package com.riskmanagement.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.riskmanagement.R;
import com.riskmanagement.adapter.MapNearAdapter;
import com.riskmanagement.http.HttpHelper;
import com.riskmanagement.http.bean.GetArData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.riskmanagement.MyApplication.MPcoordinates;
import static com.riskmanagement.MyApplication.mLocation;


public class GPSFragment extends Fragment implements View.OnClickListener{
    private MapView mapView;
    private TextView tv_gps_all;
    private TextView tv_gps_officeBuilding;
    private TextView tv_gps_area;
    private TextView tv_gps_business;
    private ListView lv_gps_near;
    private ImageView iv_gps_search;


    private BaiduMap baiduMap = null;
    // 定位相关声明
    private LocationClient locationClient = null;
    private TextView tv_gps_location;
    boolean isFirstLoc = true;// 是否首次定位
    private String longitudea;
    private String latitude;
    private String id;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String statu;

    //附近检索
    PoiNearbySearchOption option;
    PoiSearch  mPoiSearch;
    String searchKeyword = "楼";

    ArrayList<PoiInfo> nearList;
    MapNearAdapter mapNearAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gps, container, false);

        statu=getArguments().getString("statu");
        Log.e("ssh",statu+"    gpsfragment");
        initView(view);
        initData();
        return view;
    }

    private void initData() {
        sharedPreferences = getContext().getSharedPreferences("myData", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        id = sharedPreferences.getString("id", "");
        Log.d("myyID", id);
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        final Call<GetArData> getArDataCall = HttpHelper.getArData(params, new Callback<GetArData>() {
            @Override
            public void onResponse(Call<GetArData> call, Response<GetArData> response) {

                if("0".equals(statu)){
//开启定位图层
                    baiduMap.setMyLocationEnabled(true);

                    locationClient = new LocationClient(getContext()); // 实例化LocationClient类
                    setLocationOption();   //设置定位参数
                    locationClient.registerLocationListener(myListener); // 注册监听函数
                    locationClient.start(); // 开始定位
                }else{
                    try{
                        MPcoordinates = response.body().getData().getCoordinates();
                        Log.e("ssh","。"+response.body().getData().getCoordinates());
                        longitudea = response.body().getData().getCoordinates().substring(0, 3);
                        latitude = response.body().getData().getCoordinates().substring(4, 7);
                    }catch(Exception e){
                        Log.e("ssh","Gpsfragment Exception = "+e.getMessage());
                        longitudea = "119.30";
                        latitude = "26.08";
                    }
                    finally {
                        searchNeayBy();
                        LatLng latLng = new LatLng(Double.valueOf(latitude),Double.valueOf(longitudea));
                        Log.d("myLatLng", String.valueOf(latLng));
//                        Map.setMap(latLng, baiduMap);
                        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(latLng, 16);   //设置地图中心点以及缩放级别
//              MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                        markPoint();
                        baiduMap.animateMapStatus(u);
                        mhandler.sendMessageDelayed(mhandler.obtainMessage(10),50);
                    }
                }
                mLocation = response.body().getData().getCoorDesc();
                tv_gps_location.setText(mLocation);
            }

            @Override
            public void onFailure(Call<GetArData> call, Throwable t) {
                Toast.makeText(getContext(), "网络访问异常", Toast.LENGTH_LONG);
            }
        });
        /*f (mLocation == null || mLocation == "") {
            statu = 0;
            //开启定位图层
            baiduMap.setMyLocationEnabled(true);
            locationClient = new LocationClient(getContext()); // 实例化LocationClient类
            setLocationOption();   //设置定位参数
            locationClient.registerLocationListener(myListener); // 注册监听函数
            locationClient.start(); // 开始定位

        } else {
            statu = 1;*/
              /*LatLng latLng=new LatLng(Double.parseDouble(longitude),Double.parseDouble(latitude));*/


       /* }*/

    }

    private void initView(View view) {
        mapView=(MapView)view.findViewById(R.id.mv_map);
        tv_gps_all=(TextView)view.findViewById(R.id.tv_gps_all);
        tv_gps_area=(TextView)view.findViewById(R.id.tv_gps_area);
        tv_gps_business=(TextView)view.findViewById(R.id.tv_gps_business);
        tv_gps_officeBuilding=(TextView)view.findViewById(R.id.tv_gps_officeBuliding);
        tv_gps_all.setOnClickListener(onClickListener);
        tv_gps_area.setOnClickListener(onClickListener);
        tv_gps_business.setOnClickListener(onClickListener);
        tv_gps_officeBuilding.setOnClickListener(onClickListener);
        lv_gps_near=(ListView)view.findViewById(R.id.lv_gps_near);
//        iv_gps_search=(ImageView)view.findViewById(R.id.iv_gps_search);
        tv_gps_location=(TextView)view.findViewById(R.id.tv_gps_location);
        baiduMap=mapView.getMap();
        nearList = new ArrayList<PoiInfo>();

    }



    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
      /* if(statu==0)destroy();*/
        super.onDestroy();
        mapView.onDestroy();
        mapView=null;
    }

    @Override
    public void onClick(View view) {

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.tv_gps_all:
                    searchKeyword = "楼";
                    nearList.removeAll(nearList);
                    searchNeayBy();
                    mPoiSearch.searchNearby(option);
                    break;
                case R.id.tv_gps_area:
                    searchKeyword = "小区";
                    nearList.removeAll(nearList);
                    searchNeayBy();
                    mPoiSearch.searchNearby(option);
                    break;
                case R.id.tv_gps_business:
                    searchKeyword = "商店";
                    nearList.removeAll(nearList);
                    searchNeayBy();
                    mPoiSearch.searchNearby(option);
                    break;
                case R.id.tv_gps_officeBuliding:
                    searchKeyword = "写字楼";
                    nearList.removeAll(nearList);
                    searchNeayBy();
                    mPoiSearch.searchNearby(option);
                    break;
            }
        }
    };

    public BDLocationListener myListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mapView == null)

                return;

            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            baiduMap.setMyLocationData(locData);    //设置定位数据

            //保存定位信息
            MPcoordinates = location.getLatitude()+","+location.getLongitude();
            mLocation = location.getAddrStr();
            longitudea = location.getLongitude()+"";
            latitude = location.getLatitude()+"";
            searchNeayBy();
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, 16);   //设置地图中心点以及缩放级别
//              MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                markPoint();
                baiduMap.animateMapStatus(u);
                mhandler.sendMessageDelayed(mhandler.obtainMessage(10),50);
            }
            StringBuffer sb = new StringBuffer(256);
            sb.append(location.getAddrStr());//获得当前地址
            mLocation=location.getAddrStr();
            logMsg(sb.toString());
        }
        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    };
    public void logMsg(String str) {
        try {
            if (tv_gps_location != null) {
                tv_gps_location.setText(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void setLocationOption() {
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开GPS
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
        option.setCoorType("bd09ll"); // 返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(5000); // 设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true); // 返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true); // 返回的定位结果包含手机机头的方向
        locationClient.setLocOption(option);
    }
    private void destroy(){
        baiduMap.setMyLocationEnabled(false);
        locationClient.stop();
        if (locationClient!=null)locationClient.unRegisterLocationListener(myListener);
    }

    /**
     * 搜索周边地理位置
     * by hankkin at:2015-11-01 22:54:49
     */
    private void searchNeayBy() {
        option = new PoiNearbySearchOption();
        option.keyword(searchKeyword);
        option.sortType(PoiSortType.distance_from_near_to_far);
        try{

            option.location(new LatLng(Double.valueOf(latitude), Double.valueOf(longitudea)));
        }catch (Exception e){
            longitudea = "119.30";
            latitude = "26.08";
            option.location(new LatLng(26.08, 119.30));
        }
        option.radius(1000);
        option.pageCapacity(20);
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                if (poiResult != null) {
                    if (poiResult.getAllPoi()!=null&&poiResult.getAllPoi().size()>0){

                        nearList.addAll(poiResult.getAllPoi());
//                        if (nearList != null && nearList.size() > 0) {
//                            for (int i = 0; i < nearList.size(); i++) {
//                                Log.e("ssh","i  = "+i+"     =============");
//                                Log.e("ssh","address  = "+nearList.get(i).address);//address  = 福州市鼓楼区
//                                Log.e("ssh","name = "+nearList.get(i).name);// name = 福建电影制片厂公寓12座
//                                Log.e("ssh","location  = "+nearList.get(i).location);//location  = latitude: 26.08245288204895, longitude: 119.29227677222829
//                                Log.e("ssh","city = "+nearList.get(i).city);//city = 福州市
////                        isSelected.put(i, false);
//                            }
//                        }
                        if(mapNearAdapter == null){
                            mapNearAdapter = new MapNearAdapter(nearList,getActivity());
                            lv_gps_near.setAdapter(mapNearAdapter);

                            lv_gps_near.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String regex = "\\d*";
                                    Pattern p = Pattern.compile(regex);

                                    Matcher m = p.matcher(nearList.get(i).location+"");
                                    ArrayList<String> StringList =  new ArrayList<String>();
                                    while (m.find()) {
                                        if (!"".equals(m.group()))
                                            StringList.add(m.group());

                                    }
                                    latitude = StringList.get(0)+"."+StringList.get(1);
                                    longitudea = StringList.get(2)+"."+StringList.get(3);
                                    MPcoordinates = latitude+","+longitudea;
                                    mLocation = nearList.get(i).name+"";
                                    mhandler.sendEmptyMessage(20);
                                }
                            });
                        }else{
                            mapNearAdapter.notifyDataSetChanged();
                        }

//                Message msg = new Message();
//                msg.what = 0;
//                handler.sendMessage(msg);
                    }

                }
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }
        });
    }

    /**
     *设置覆盖mark
     */
    public void markPoint(){
        //定义Maker坐标点
        LatLng point = new LatLng(Double.valueOf(latitude), Double.valueOf(longitudea));
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_radio_checked);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions Overlayoption = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        baiduMap.addOverlay(Overlayoption);
    }

    Handler mhandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 10:
                    mPoiSearch.searchNearby(option);
                    break;
                case 20:
                    LatLng latLng = new LatLng(Double.valueOf(latitude),Double.valueOf(longitudea));
                    Log.d("myLatLng", String.valueOf(latLng));
                    MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(latLng, 16);   //设置地图中心点以及缩放级别
                    baiduMap.clear();
//                    clearMarker();
                    markPoint();
                    tv_gps_location.setText(mLocation);
                    baiduMap.animateMapStatus(u);
                    break;
            }
        }
    };

}

