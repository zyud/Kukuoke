package pennapps.com.kukuoke;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import pennapps.com.kukuoke.R;

/**
 * Created by zyud on 1/19/2018.
 */

public class Tab3 extends Fragment {
    private UserSongs userSongs;
    private int tabNum;
    private ListView listView;
    public static CustomListAdapter claTab3;
    private boolean multiSelect = false;
    private int selectedItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3, container, false);

        tabNum = 3;
        userSongs = UserSongs.getUserSongsInstance();

        if (!MainActivity.tab3AdapterInitialized) {
            fillSongs();
            MainActivity.tab3AdapterInitialized = true;
        }

        selectedItem = -1;

        claTab3 = new CustomListAdapter(getActivity(), userSongs.getList(tabNum), R.layout.listview_row);
        listView = (ListView) rootView.findViewById(R.id.lv_tab3);
        listView.setAdapter(claTab3);

        View footerView =  ((LayoutInflater)getContext().getSystemService(getContext()
                .LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer, null, false);
        listView.addFooterView(footerView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == userSongs.getList(tabNum).size()) {
                    Intent intent = new Intent(getActivity(), AddSongActivity.class);
                    intent.putExtra("tabNum","tab3");
                    startActivity(intent);
                } else {
                    selectedItem = position;
                    ((AppCompatActivity)view.getContext()).startSupportActionMode(actionModeCallbacks);
                }
            }
        });

        return rootView;
    }

    private ActionMode.Callback actionModeCallbacks = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu){
            menu.add(0, 0, 2, "DEL");
            menu.add(0, 1, 0,"<<");
            menu.add(0, 2, 1,"<");
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (selectedItem >= 0) {
                switch(item.getItemId()) {
                    case 0:
                        userSongs.deleteSong(selectedItem, tabNum);
                        claTab3.notifyDataSetChanged();
                        mode.finish();
                        return true;
                    case 1:
                        userSongs.moveSong(getContext(), selectedItem, tabNum, 1);
                        claTab3.notifyDataSetChanged();
                        Tab1.claTab1.notifyDataSetChanged();
                        mode.finish();
                        return true;
                    case 2:
                        userSongs.moveSong(getContext(), selectedItem, tabNum, 2);
                        claTab3.notifyDataSetChanged();
                        Tab2.claTab2.notifyDataSetChanged();
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            } else {
                mode.finish();
                return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

        }
    };

    private void fillSongs(){
        userSongs.addSong(getContext(), "Never Gonna Give You Up",
                "Rick Astley", tabNum);
        userSongs.addSong(getContext(), "Kiss", "Prince", tabNum);
    }
}
