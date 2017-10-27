/*
 * Copyright 2017 Kentico s.r.o. and Richard Sustek
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.kentico.delivery.core.query.taxonomy;

import com.kentico.delivery.core.config.DeliveryClientConfig;
import com.kentico.delivery.core.models.exceptions.KenticoCloudException;
import com.kentico.delivery.core.models.taxonomy.DeliveryTaxonomyResponse;
import com.kentico.delivery.core.models.type.DeliveryTypeResponse;
import com.kentico.delivery.core.query.type.BaseTypeQuery;
import com.kentico.delivery.core.request.IRequestService;

import org.json.JSONObject;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class SingleTaxonomyQuery extends BaseTypeQuery {

    private static final String URL_PATH = "/taxonomies/";
    private final String taxonomyCodename;

    public SingleTaxonomyQuery(DeliveryClientConfig config, IRequestService requestService, String taxonomyCodename) {
        super(config, requestService);
        this.taxonomyCodename = taxonomyCodename;
    }

    @Override
    public String getQueryUrl(){
        String action = URL_PATH + this.taxonomyCodename;

        return this.queryService.getUrl(action, parameters);
    }

    // observable
    public Observable<DeliveryTaxonomyResponse> get() {
        return this.queryService.<JSONObject>getRequest(this.getQueryUrl())
                .map(new Function<JSONObject, DeliveryTaxonomyResponse>() {
                    @Override
                    public DeliveryTaxonomyResponse apply(JSONObject jsonObject) throws KenticoCloudException {
                        try {
                            return responseMapService.mapDeliveryTaxonomyResponse(jsonObject);
                        } catch (IOException ex) {
                            throw new KenticoCloudException("Could not get taxonomy response with error: " + ex.getMessage(), ex);
                        }
                    }
                });
    }
}
