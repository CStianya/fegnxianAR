package com.riskmanagement;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

import java.util.ArrayList;

/**
 * Created by ssh on 2017/6/20.
 */

public class aa extends Activity {
    MapView mMapView = null;
    PoiSearch poiSearch;
    PoiNearbySearchOption nearbySearchOption;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.aa);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        BaiduMap mBaidumap = mMapView.getMap();
//设定中心点坐标
        LatLng cenpt = new LatLng(26.08,119.30);
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(cenpt)
                .zoom(18)
                .build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);



//改变地图状态
        mBaidumap.setMapStatus(mMapStatusUpdate);
        search();
//        mPoiSearch = PoiSearch.newInstance();
    }

    private void search() {
        poiSearch = PoiSearch.newInstance();

        nearbySearchOption = new PoiNearbySearchOption();
        nearbySearchOption.location(new LatLng(26.08, 119.30));//设置中心点
        nearbySearchOption.radius(1000);//设置半径 米
        nearbySearchOption.keyword("写字楼");//关键字

        poiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {

            @Override
            public void onGetPoiResult(PoiResult poiResult) {

                if (poiResult != null) {
                    Log.e("ssh","dddddddddddddddd");
                    if (poiResult.getAllPoi()!=null&&poiResult.getAllPoi().size()>0){
                        Log.e("ssh","fffffffffffffffff");
                        ArrayList<PoiInfo> nearList = new ArrayList<PoiInfo>();
                        nearList.addAll(poiResult.getAllPoi());
                        if (nearList != null && nearList.size() > 0) {
                            Log.e("ssh",nearList.size()+ "  fffffffffffffffff");
                            for (int i = 0; i < nearList.size(); i++) {
                                Log.e("ssh","i  = "+i+"     =============");
                                Log.e("ssh","address  = "+nearList.get(i).address);
                                Log.e("ssh","name = "+nearList.get(i).name);
                                Log.e("ssh","location  = "+nearList.get(i).location);
                                Log.e("ssh","city = "+nearList.get(i).city);

//                        isSelected.put(i, false);
                            }
                        }
//                Message msg = new Message();
//                msg.what = 0;
//                handler.sendMessage(msg);
                    }

                }
//                if(result==null||SearchResult.ERRORNO.RESULT_NOT_FOUND==result.error){
//                    Toast.makeText(getApplicationContext(), "未搜索到结果", 0).show();
//                    return;
//                }
//                PoiOverlay overlay = new MyPoiOverlay(baiduMap);// 搜索poi的覆盖物
//                baiduMap.setOnMarkerClickListener(overlay);// 把事件分发给overlay，overlay才能处理点击事件
//                overlay.setData(result);// 设置结果
//                overlay.addToMap();// 把搜索的结果添加到地图中
//                overlay.zoomToSpan();// 缩放地图，使所有Overlay都在合适的视野内 注： 该方法只对Marker类型的overlay有效
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult result) {
                if(result==null|| SearchResult.ERRORNO.RESULT_NOT_FOUND==result.error){
                    Toast.makeText(getApplicationContext(), "未搜索到结果", Toast.LENGTH_SHORT).show();
                    return;
                }

                String text = result.getAddress()+ "::" + result.getCommentNum() + result.getEnvironmentRating();
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }
        });
        handler.sendMessageDelayed(handler.obtainMessage(10),50);

    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 10:
                    poiSearch.searchNearby(nearbySearchOption);
                    break;
            }
        }
    };


    public void onClick(View view){
        poiSearch.searchNearby(nearbySearchOption);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
}




