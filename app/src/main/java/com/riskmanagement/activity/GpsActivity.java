package com.riskmanagement.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.baidu.mapapi.BMapManager.getContext;
import static com.riskmanagement.MyApplication.MPcoordinates;
import static com.riskmanagement.MyApplication.mLocation;

/**
 * Created by Administrator on 2017/6/3.
 */

public class GpsActivity extends BaseActivity {
    private MapView mapView;
    private Button btn_gpsreview_ok;
    private TextView tv_gps_all;
    private TextView tv_gps_officeBuilding;
    private TextView tv_gps_area;
    private TextView tv_gps_business;
    private ListView lv_gps_near;
    private ImageView iv_gps_search;

    String latitude,longitudea;

    private BaiduMap baiduMap = null;
    // 定位相关声明
    private LocationClient locationClient = null;
    private TextView tv_gps_location;
    boolean isFirstLoc = true;// 是否首次定位

    //附近检索
    PoiNearbySearchOption option;
    PoiSearch  mPoiSearch;
    String searchKeyword = "楼";

    ArrayList<PoiInfo> nearList;
    MapNearAdapter mapNearAdapter;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_gps);
        initTop("GPS定位");
        initView();
    }
    private void initView() {
        nearList = new ArrayList<PoiInfo>();
        mapView=(MapView)findViewById(R.id.mv_review_map);
        lv_gps_near=(ListView)findViewById(R.id.lv_gps_near);
        tv_gps_all=(TextView)findViewById(R.id.tv_gps_all);
        tv_gps_area=(TextView)findViewById(R.id.tv_gps_area);
        tv_gps_business=(TextView)findViewById(R.id.tv_gps_business);
        tv_gps_officeBuilding=(TextView)findViewById(R.id.tv_gps_officeBuliding);
        tv_gps_all.setOnClickListener(onClickListener);
        tv_gps_area.setOnClickListener(onClickListener);
        tv_gps_business.setOnClickListener(onClickListener);
        tv_gps_officeBuilding.setOnClickListener(onClickListener);
        /* iv_gps_search=(ImageView)view.findViewById(R.id.iv_gps_search);*/
        tv_gps_location=(TextView)findViewById(R.id.tv_gpsreview__location);
        btn_gpsreview_ok=(Button)findViewById(R.id.btn_gpsreview_ok);
        btn_gpsreview_ok.setOnClickListener(onClickListener);
        baiduMap=mapView.getMap();
        //开启定位图层
        baiduMap.setMyLocationEnabled(true);
        locationClient = new LocationClient(getApplicationContext()); // 实例化LocationClient类
        setLocationOption();   //设置定位参数
        locationClient.registerLocationListener(myListener); // 注册监听函数
        locationClient.start(); // 开始定位
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_gpsreview_ok:
                    finish();
                    break;
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

            mLocation = location.getAddrStr();
            latitude = location.getLatitude()+"";
            longitudea = location.getLongitude()+"";
            Log.e("ssh","   "+longitudea+"   "+latitude);
            searchNeayBy();
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, 16);   //设置地图中心点以及缩放级别
//              MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
//                markPoint();
                baiduMap.animateMapStatus(u);
                mhandler.sendMessageDelayed(mhandler.obtainMessage(10),1000);
            }
            StringBuffer sb = new StringBuffer(256);
            sb.append(location.getAddrStr());//获得当前地址
            logMsg(sb.toString());
        }
        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    };

    /**
     * 搜索周边地理位置
     * by hankkin at:2015-11-01 22:54:49
     */
    private void searchNeayBy() {
        option = new PoiNearbySearchOption();
        option.keyword(searchKeyword);
        option.sortType(PoiSortType.distance_from_near_to_far);
        Log.e("ssh","   "+longitudea+"   "+latitude);
        option.location(new LatLng(Double.valueOf(latitude), Double.valueOf(longitudea)));
        option.radius(1000);
        option.pageCapacity(20);
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                if (poiResult != null) {
                    if (poiResult.getAllPoi()!=null&&poiResult.getAllPoi().size()>0){

                        nearList.addAll(poiResult.getAllPoi());
                        if (nearList != null && nearList.size() > 0) {
                            for (int i = 0; i < nearList.size(); i++) {
                                Log.e("ssh","i  = "+i+"     =============");
                                Log.e("ssh","address  = "+nearList.get(i).address);//address  = 福州市鼓楼区
                                Log.e("ssh","name = "+nearList.get(i).name);// name = 福建电影制片厂公寓12座
                                Log.e("ssh","location  = "+nearList.get(i).location);//location  = latitude: 26.08245288204895, longitude: 119.29227677222829
                                Log.e("ssh","city = "+nearList.get(i).city);//city = 福州市
//                        isSelected.put(i, false);
                            }
                        }
                        if(mapNearAdapter == null){
                            mapNearAdapter = new MapNearAdapter(nearList,GpsActivity.this);
                            lv_gps_near.setAdapter(mapNearAdapter);

                            lv_gps_near.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    baiduMap.setMyLocationEnabled(false);
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
    public void logMsg(String str) {
        try {
            if (tv_gps_location != null) {
                tv_gps_location.setText(str);
                mLocation = str;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        baiduMap.addOverlay(option);
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
        destroy();
        super.onDestroy();
        mapView.onDestroy();
        mapView=null;
    }

}
