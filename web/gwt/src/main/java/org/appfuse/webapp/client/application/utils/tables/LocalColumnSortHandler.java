package org.appfuse.webapp.client.application.utils.tables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent.Handler;
import com.google.gwt.view.client.HasData;

/**
 * 
 * @author ivangsa
 *
 * @param <T>
 */
public abstract class LocalColumnSortHandler<T> implements Handler {

    private final HasData<T> hasData;
    private final Map<Column<?, ?>, Comparator<T>> comparators = new HashMap<Column<?, ?>, Comparator<T>>();

    /**
     * @param cellTable
     */
    public LocalColumnSortHandler(HasData<T> hasData) {
        super();
        this.hasData = hasData;
    }

    /**
     * Returns the comparator that has been set for the specified column, or
     * null if no comparator has been set.
     * 
     * @param column
     *            the {@link Column}
     */
    public Comparator<T> getComparator(final Column<T, ?> column) {
        if (!comparators.containsKey(column)) {
            setComparator(column, new Comparator<T>() {
                @Override
                public int compare(T o1, T o2) {
                    Object value1 = column.getValue(o1);
                    Object value2 = column.getValue(o2);
                    if (value1 == null) {
                        return -1;
                    }
                    if (value2 == null) {
                        return 1;
                    }
                    if (value1 instanceof Number || value2 instanceof Number) {
                        // numeric comparison
                        return ((Number) value1).intValue() - ((Number) value2).intValue();
                    } else {
                        // string comparison
                        return value1.toString().compareTo(value2.toString());
                    }
                }
            });
        }

        return comparators.get(column);
    }

    public abstract List<T> getList();

    public void onColumnSort(ColumnSortEvent event) {
        // Get the sorted column.
        Column<T, ?> column = (Column<T, ?>) event.getColumn();
        if (column == null) {
            return;
        }

        // Get the comparator.
        final Comparator<T> comparator = getComparator(column);
        if (comparator == null) {
            return;
        }

        // Sort using the comparator.
        List<T> sortedList = new ArrayList<T>(getList());
        if (event.isSortAscending()) {
            Collections.sort(sortedList, comparator);
        } else {
            Collections.sort(sortedList, new Comparator<T>() {
                public int compare(T o1, T o2) {
                    return -comparator.compare(o1, o2);
                }
            });
        }
        hasData.setRowData(0, sortedList);
    }

    /**
     * Set the comparator used to sort the specified column in ascending order.
     * 
     * @param column
     *            the {@link Column}
     * @param comparator
     *            the {@link Comparator} to use for the {@link Column}
     */
    public void setComparator(Column<T, ?> column, Comparator<T> comparator) {
        comparators.put(column, comparator);
    }
}