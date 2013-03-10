package org.appfuse.webapp.pages.admin;

import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.model.PropertyModel;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Basic sortable data provider comparator.
 *
 * @param <T> comparable object type
 */
//TODO: MZA: Isn't it an overkill (PropertyModel is created for every comparison)
// AbstractUserComparator as an alternative
@SuppressWarnings("unchecked")
class SortableDataProviderComparator<T> implements Comparator<T>, Serializable {

    private final SortParam<String> sortParam;

    public SortableDataProviderComparator(SortParam<String> sortParam) {
        this.sortParam = sortParam;
    }

    public int compare(T o1, T o2) {
        PropertyModel<Comparable> model1 = new PropertyModel<Comparable>(o1, sortParam.getProperty());
        PropertyModel<Comparable> model2 = new PropertyModel<Comparable>(o2, sortParam.getProperty());

        //Compares comparable object from model1 with another from model2 - both has the same type - it's
        //the same property
        int result = (model1.getObject()).compareTo(model2.getObject());

        if (!sortParam.isAscending()) {
            result = -result;
        }
        return result;
    }

    public static <T> SortableDataProviderComparator<T> getInstanceForSortParam(SortParam<String> sortParam) {
        return new SortableDataProviderComparator<T>(sortParam);
    }
}
