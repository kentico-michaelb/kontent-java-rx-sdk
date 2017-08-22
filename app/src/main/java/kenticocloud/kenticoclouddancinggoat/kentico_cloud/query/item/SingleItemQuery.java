package kenticocloud.kenticoclouddancinggoat.kentico_cloud.query.item;

import android.support.annotation.NonNull;

import com.androidnetworking.common.Priority;
import com.rx2androidnetworking.Rx2AndroidNetworking;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import kenticocloud.kenticoclouddancinggoat.kentico_cloud.config.DeliveryClientConfig;
import kenticocloud.kenticoclouddancinggoat.kentico_cloud.interfaces.item.common.IField;
import kenticocloud.kenticoclouddancinggoat.kentico_cloud.interfaces.item.item.IContentItem;
import kenticocloud.kenticoclouddancinggoat.kentico_cloud.interfaces.item.item.IContentItemSystemAttributes;
import kenticocloud.kenticoclouddancinggoat.kentico_cloud.models.ContentItem;
import kenticocloud.kenticoclouddancinggoat.kentico_cloud.models.common.Filters;
import kenticocloud.kenticoclouddancinggoat.kentico_cloud.models.common.Parameters;
import kenticocloud.kenticoclouddancinggoat.kentico_cloud.models.item.DeliveryItemListingResponse;
import kenticocloud.kenticoclouddancinggoat.kentico_cloud.models.item.DeliveryItemResponse;
import kenticocloud.kenticoclouddancinggoat.kentico_cloud.models.item.RawModels;
import kenticocloud.kenticoclouddancinggoat.kentico_cloud.utils.JsonHelper;
import kenticocloud.kenticoclouddancinggoat.kentico_cloud.utils.MapHelper;

/**
 * Created by RichardS on 17. 8. 2017.
 */
public class SingleItemQuery extends BaseItemQuery{

    private final String _itemsUrlAction = "/items/";
    private final String _itemCodename;

    public SingleItemQuery(@NonNull DeliveryClientConfig config, @NonNull String itemCodename) {
        super(config);
        _itemCodename = itemCodename;
    }

    // parameters
    public SingleItemQuery elementsParameter(@NonNull List<String> elements){
        this._parameters.add(new Parameters.ElementsParameter(elements));
        return this;
    }

    public SingleItemQuery languageParameter(@NonNull String language){
        this._parameters.add(new Parameters.LanguageParameter(language));
        return this;
    }

    public SingleItemQuery depthParameter(int limit){
        this._parameters.add(new Parameters.DepthParameter(limit));
        return this;
    }

    // url builder
    private String getSingleItemUrl(){
        String action = _itemsUrlAction + _itemCodename;

        return getUrl(action, _parameters);
    }

    // observable
    public Observable<DeliveryItemResponse> get() {
        String url = getSingleItemUrl();

        return Rx2AndroidNetworking.get(url)
                .setPriority(Priority.MEDIUM)
                .build()
                .getObjectObservable(RawModels.DeliveryItemResponseRaw.class)
                .map(new Function<RawModels.DeliveryItemResponseRaw, DeliveryItemResponse>() {
                    @Override
                    public DeliveryItemResponse apply(RawModels.DeliveryItemResponseRaw responseRaw) throws Exception {
                        if (responseRaw.item == null) {
                            return null;
                        }

                        // get item
                        IContentItem mappedItem = _itemMapService.mapItem(responseRaw.item);

                        // map properties
                        mappedItem.mapProperties();

                        return new DeliveryItemResponse(mappedItem);
                    }
                });
    }
}