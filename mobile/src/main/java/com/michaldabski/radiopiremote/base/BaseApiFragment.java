package com.michaldabski.radiopiremote.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.michaldabski.radiopiremote.R;
import com.michaldabski.radiopiremote.api.ApiConfigurationError;
import com.michaldabski.radiopiremote.api.ApiUrlBuilder;
import com.michaldabski.radiopiremote.api.requests.GsonResponseListener;


/**
 * Base class for fragments presenting data from API
 * Created by Michal on 25/11/2016.
 * @param <RT> Type of the Response object
 * @param <IT> Type of the item object
 */
public abstract class BaseApiFragment<RT, IT> extends BaseFragment implements GsonResponseListener<RT>, AdapterView.OnItemClickListener {
    private ArrayAdapter<IT> adapter;
    private ListView listView = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.adapter = createAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = (ListView) view.findViewById(android.R.id.list);
        listView.setOnItemClickListener(this);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            sendRequest();
        } catch (ApiConfigurationError e) {
            // not configured
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        listView = null;
    }

    protected void setProgressVisible(boolean visible) {
        if (isAdded()) {
            final int visibility = visible ? View.VISIBLE : View.GONE;
            getView().findViewById(android.R.id.progress).setVisibility(visibility);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        super.onErrorResponse(error);
        setProgressVisible(false);
    }

    public void sendRequest(){
        setProgressVisible(true);
        sendRequest(createRequest());
    }

    @Override
    public void onResponse(RT responseObject) {
        setProgressVisible(false);
    }

    public ArrayAdapter<IT> getAdapter() {
        return adapter;
    }

    protected ApiUrlBuilder getApiUrlBuilder() throws ApiConfigurationError{
        return getPiRemoteApplication().getApiUrlBuilder();
    }

    /**
     * Build request object to fetch the response for this fragment
     */
    @NonNull
    protected abstract Request<RT> createRequest();

    /**
     * Create empty adapter
     * @return
     */
    @NonNull
    protected abstract ArrayAdapter<IT> createAdapter();
}
