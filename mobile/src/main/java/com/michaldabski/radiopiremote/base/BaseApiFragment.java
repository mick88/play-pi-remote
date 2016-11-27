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
import com.michaldabski.radiopiremote.api.models.ListResponse;
import com.michaldabski.radiopiremote.api.requests.GsonResponseListener;


/**
 * Base class for fragments presenting data from API
 * Created by Michal on 25/11/2016.
 * @param <RT> Type of the Response object
 * @param <IT> Type of the item object
 */
public abstract class BaseApiFragment<RT, IT> extends BaseFragment implements GsonResponseListener<RT>, AdapterView.OnItemClickListener, AbsListView.OnScrollListener, AdapterView.OnItemLongClickListener {
    public static final int DEFAULT_PAGE = 1;
    /**
     * Prefetch next page this many items before the end
     */
    public static final int NEXTPAGE_PREFETCH = 5;
    private ArrayAdapter<IT> adapter;
    private ListView listView = null;
    private Request<RT> currentRequest = null;
    boolean hasMorePages = false;
    int nextPageNumber = DEFAULT_PAGE;
    private View progressFooter;

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
        listView.setOnItemLongClickListener(this);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(this);

        final LayoutInflater inflater = LayoutInflater.from(getContext());
        progressFooter = inflater.inflate(R.layout.progress, listView, false);
        listView.addFooterView(progressFooter, null, false);
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
        progressFooter = null;
    }

    protected void setProgressVisible(boolean visible) {
        if (isAdded()) {
            final int visibility = visible ? View.VISIBLE : View.GONE;
            progressFooter.setVisibility(visibility);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        super.onErrorResponse(error);
        setProgressVisible(false);
    }

    public void sendRequest(){
        setProgressVisible(true);
        currentRequest = createRequest(DEFAULT_PAGE);
        sendRequest(currentRequest);
    }

    @Override
    public void onResponse(RT response) {
        currentRequest = null;
        setProgressVisible(false);

        if (response instanceof ListResponse) {
            final ListResponse listResponse = (ListResponse) response;
            if (listResponse.getPrevious() == null) {
                // Clean list if this is the first page
                adapter.clear();
            }
            hasMorePages = listResponse.getNext() != null;
            nextPageNumber++;
        }
    }

    public ArrayAdapter<IT> getAdapter() {
        return adapter;
    }

    protected ApiUrlBuilder getUrlBuilder() throws ApiConfigurationError{
        return getPiRemoteApplication().getApiUrlBuilder();
    }

    /**
     * Build request object to fetch the response for this fragment
     */
    @NonNull
    protected abstract Request<RT> createRequest(int page);

    /**
     * Create empty adapter
     * @return
     */
    @NonNull
    protected abstract ArrayAdapter<IT> createAdapter();

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int lastVisibleItem = firstVisibleItem + visibleItemCount + NEXTPAGE_PREFETCH;
        if (hasMorePages && currentRequest == null && lastVisibleItem >= totalItemCount) {
            currentRequest = createRequest(nextPageNumber);
            sendRequest(currentRequest);
            progressFooter.setVisibility(View.VISIBLE);
        }
    }

}
