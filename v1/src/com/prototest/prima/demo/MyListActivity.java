//package com.prototest.prima.demo;
///*
// * Copyright 2012 The Android Open Source Project
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.app.Activity;
//import android.content.Context;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.prototest.prima.R;
//
//public class MyListActivity extends Activity{
//    ListView listView;
//    List<MyData> dataSource;
//    MyListAdapter adapter;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        this.setContentView(R.layout.list_layout);
//        listView = (ListView) findViewById(R.id.my_list);
//        dataSource = new ArrayList<MyData>();
//        adapter = new MyListAdapter(this, R.layout.item_layout, dataSource);
//        listView.setAdapter(adapter);
//    }
// 
//    // private Adapter for my list
//    private static class MyListAdapter extends BaseAdapter {
//        private Activity parentActivity;
//        private int itemLayoutId;
//        private List<MyData> dataSource;
//        private LayoutInflater inflater;
// 
//        // constructor for adapter
//            public MyListAdapter(Activity activity, int layoutId, List<MyData> ds){
//            parentActivity = activity;
//            itemLayoutId = layoutId;
//            dataSource = ds;
//            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            }
// 
//        @Override
//        public View getView(int pos, View convertView, ViewGroup parentView){
//            View view = null;
//            if(convertView == null){
//                view = inflater.inflate(itemLayoutId, parentView, false);
//                TextView textView = (TextView)view.findViewById(R.id.data_item);
//                String data = dataSource.get(pos).getItem();
//                textView.setText(data);
//            }else{
//                view = convertView;
//            }
//            return view;
//        }
//
//		@Override
//		public int getCount() {
//			// TODO Auto-generated method stub
//			return 0;
//		}
//
//		@Override
//		public Object getItem(int arg0) {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public long getItemId(int arg0) {
//			// TODO Auto-generated method stub
//			return 0;
//		}
//    }
//}