package com.mdd.aleksandrfilcenko.pedometer;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.view.MaterialListView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SuperAwesomeCardFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String ARG_POSITION = "position";
    private MaterialListView mListView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CircleProgressBar progress;
    private String user_id, hash, today = "today", global = "global";
    private int position;

    public static SuperAwesomeCardFragment newInstance(int position) {
        SuperAwesomeCardFragment f = new SuperAwesomeCardFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        user_id = sharedPreferences.getString("user_id", "");
        hash = sharedPreferences.getString("hash", "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.second_card_test, null);

        mListView = (MaterialListView) rootView.findViewById(R.id.material_listview);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setVisibility(View.GONE);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.mainLight);

        progress = (CircleProgressBar) rootView.findViewById(R.id.progressBar);
        progress.setColorSchemeResources(R.color.mainLight);
        progress.setVisibility(View.VISIBLE);

        if (position == 0) {

            getDataPosition(today);

        } else {

            getDataPosition(global);

        }

        return rootView;

    }


    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);

                if (position == 0) {

                    getDataRefsreshPosition(today);

                } else {

                    getDataRefsreshPosition(global);

                }

            }
        }, 2000);
    }

    private void getDataPosition(String subaction) {

        RequestParams params = new RequestParams();
        params.put("user_id", user_id);
        params.put("hash", hash);
        params.put("subaction", subaction);
        params.put("secret", Constants.SECRET);

        ApiRest.post("main.php?action=top", params, new JsonHttpResponseHandler() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {

                    JSONObject responseObjectGifts = new JSONObject(response.toString());
                    JSONArray responseDataGifts = new JSONArray(responseObjectGifts.get("data").toString());

                    progress.setVisibility(View.GONE);
                    swipeRefreshLayout.setVisibility(View.VISIBLE);

                    for (int i = 0; i < responseDataGifts.length(); i++) {

                       JSONObject jsonobject = responseDataGifts.getJSONObject(i);
                       Card secondcard = new Card.Builder(getContext())
                                .withProvider(new TopCardProvider())
                                .setLayout(R.layout.top_cardview)
                                .setAvatar(jsonobject.getString("avatar"))
                                .setCrown(jsonobject.getString("icon"))
                                .setSteps(jsonobject.getString("steps"))
                                .setName(jsonobject.getString("name"))
                                .setNumberTop(i + 1)
                                .endConfig().build();

                       mListView.getAdapter().add(secondcard);
                       mListView.scrollToPosition(0);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getDataRefsreshPosition(String subaction) {

        RequestParams params = new RequestParams();
        params.put("user_id", user_id);
        params.put("hash", hash);
        params.put("subaction", subaction);
        params.put("secret", Constants.SECRET);

        ApiRest.post("main.php?action=top", params,

                new JsonHttpResponseHandler() {

                    @TargetApi(Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                        try {

                            JSONObject responseObjectGifts = new JSONObject(response.toString());
                            JSONArray responseDataGifts = new JSONArray(responseObjectGifts.get("data").toString());

                            for (int i = 0; i < responseDataGifts.length(); i++) {

                                Card secondcard = mListView.getAdapter().getCard(i);
                                assert secondcard != null;

                                TopCardProvider cardProv = (TopCardProvider) secondcard.getProvider();

                                mListView.getAdapter().notifyDataSetChanged();

                                JSONObject jsonobject = responseDataGifts.getJSONObject(i);
                                cardProv.setLayout(R.layout.top_cardview);
                                cardProv.setAvatar(jsonobject.getString("avatar"));
                                cardProv.setCrown(jsonobject.getString("icon"));
                                cardProv.setSteps(jsonobject.getString("steps"));
                                cardProv.setName(jsonobject.getString("name"));
                                cardProv.setNumberTop(i + 1);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
