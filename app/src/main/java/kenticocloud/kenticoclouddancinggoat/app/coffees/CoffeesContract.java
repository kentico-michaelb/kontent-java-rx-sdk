package kenticocloud.kenticoclouddancinggoat.app.coffees;

import java.util.List;

import kenticocloud.kenticoclouddancinggoat.app.core.IBasePresenter;
import kenticocloud.kenticoclouddancinggoat.app.core.IBaseView;
import kenticocloud.kenticoclouddancinggoat.data.models.Coffee;

/**
 * Created by RichardS on 15. 8. 2017.
 */

interface CoffeesContract {
    interface View extends IBaseView<Presenter> {
        void showCoffees(List<Coffee> coffees);
    }

    interface Presenter extends IBasePresenter {
        void loadCoffees();
    }
}