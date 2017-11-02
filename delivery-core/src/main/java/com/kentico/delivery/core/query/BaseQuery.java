/*
 * Copyright 2017 Kentico s.r.o. and Richard Sustek
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.kentico.delivery.core.query;

import com.kentico.delivery.core.adapters.IHttpAdapter;
import com.kentico.delivery.core.adapters.IRxAdapter;
import com.kentico.delivery.core.config.IDeliveryConfig;
import com.kentico.delivery.core.interfaces.item.common.IQueryConfig;
import com.kentico.delivery.core.interfaces.item.common.IQueryParameter;
import com.kentico.delivery.core.models.common.IDeliveryResponse;
import com.kentico.delivery.core.services.IQueryService;
import com.kentico.delivery.core.services.QueryService;
import com.kentico.delivery.core.services.ResponseMapService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public abstract class BaseQuery<TQuery extends BaseQuery> {

    protected List<IQueryParameter> parameters = new ArrayList<>();
    protected IQueryConfig queryConfig;

    protected IDeliveryConfig config;
    protected ResponseMapService responseMapService;
    protected IQueryService queryService;

    protected BaseQuery(IDeliveryConfig config, IRxAdapter rxAdapter, IHttpAdapter httpAdapter){
        this.config = config;
        this.responseMapService = new ResponseMapService(config);
        this.queryService = new QueryService(config, rxAdapter, httpAdapter);

        // assign default query configuration, this can be overridden by setters
        this.queryConfig = config.getDefaultQueryConfig();
    }

    /**
     * Adds parameter to query
     * @param queryParameter Query parameter
     * @return Query
     */
    public BaseQuery<TQuery> addParameter(IQueryParameter queryParameter){
        this.parameters.add(queryParameter);
        return this;
    }

    /**
     * Indicates if loading for new content header is set
     * @param wait True or false
     * @return Query
     */
    public BaseQuery<TQuery> setWaitForLoadingNewContent(boolean wait){
        this.queryConfig.setWaitForLoadingNewContent(wait);
        return this;
    }

    /**
     * Indicates if preview mode for this query is used
     * @param enablePreview Enabled or disabled
     * @return Query
     */
    public BaseQuery<TQuery> setUsePreviewMode(boolean enablePreview){
        this.queryConfig.setUsePreviewMode(enablePreview);
        return this;
    }

    /**
     * Gets query configuration
     * @return Query configuration
     */
    public IQueryConfig getQueryConfig(){
        return this.queryConfig;
    }

    /**
     * Sets query configuration
     * @param queryConfig Query configuration
     */
    public void setQueryConfig(IQueryConfig queryConfig){
        this.queryConfig = queryConfig;
    }

    /**
     * Gets url of query action
     * @return Url used to hit Kentico Cloud
     */
    public abstract String getQueryUrl();

    /**
     * Gets observable to fetch IDeliveryResposne from Kentico Cloud
     * @return Observable to get Delivery response
     */
    public abstract Observable<? extends IDeliveryResponse> getObservable();

    /**
     * Gets delivery response
     * @return Delivery response
     */
    public abstract IDeliveryResponse get();
}
