/**
 * 
 */
package org.appfuse.webapp.client.ui.users.editUser;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.appfuse.webapp.client.proxies.RoleProxy;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.dom.client.Element;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.CheckBox;

/**
 * @author ivangsa
 *
 */
public class RolesListBox extends CellList<RoleProxy> implements Editor<Set<RoleProxy>>, IsEditor<LeafValueEditor<Set<RoleProxy>>>, LeafValueEditor<Set<RoleProxy>> {

    private final RoleCheckboxCell roleCheckboxCell;

    public RolesListBox() {
        this(new RoleCheckboxCell());
        setSelectionModel(null);
    }

    private RolesListBox(RoleCheckboxCell roleCheckboxCell) {
        super(new RoleCell(Arrays.asList(roleCheckboxCell, new RoleNameCell())));
        this.roleCheckboxCell = roleCheckboxCell;
    }

    @Override
    public LeafValueEditor<Set<RoleProxy>> asEditor() {
        return this;
    }

    @Override
    public void setValue(Set<RoleProxy> value) {
        roleCheckboxCell.setRolesSet(value);
        this.redraw();
    }

    @Override
    public Set<RoleProxy> getValue() {
        return roleCheckboxCell.getRolesSet();
    }

    public void setReadonly(boolean readonly) {
        roleCheckboxCell.setReadonly(readonly);
    }
}

class RoleCell extends CompositeCell<RoleProxy> {

    public RoleCell(List<HasCell<RoleProxy, ?>> hasCells) {
        super(hasCells);
    }

    @Override
    public void render(Context context, RoleProxy value, SafeHtmlBuilder sb) {
        sb.appendHtmlConstant("<table><tbody><tr>");
        super.render(context, value, sb);
        sb.appendHtmlConstant("</tr></tbody></table>");
    }

    @Override
    protected Element getContainerElement(Element parent) {
        // Return the first TR element in the table.
        return parent.getFirstChildElement().getFirstChildElement().getFirstChildElement();
    }

    @Override
    protected <X> void render(Context context, RoleProxy value, SafeHtmlBuilder sb, HasCell<RoleProxy, X> hasCell) {
        Cell<X> cell = hasCell.getCell();
        sb.appendHtmlConstant("<td>");
        cell.render(context, hasCell.getValue(value), sb);
        sb.appendHtmlConstant("</td>");
    }
}

class RoleCheckboxCell implements HasCell<RoleProxy, Boolean> {

    private boolean readonly = false;
    private final Set<RoleProxy> rolesSet = new HashSet<RoleProxy>();
    private final CheckboxCell cell = new CheckboxCell(true, true);
    private final AbstractCell<Boolean> readonlyCell = new AbstractCell<Boolean>() {
        @Override
        public void render(com.google.gwt.cell.client.Cell.Context context, Boolean value, SafeHtmlBuilder sb) {
            CheckBox checkBox = new CheckBox();
            checkBox.setValue(value);
            checkBox.setEnabled(false);
            sb.append(SafeHtmlUtils.fromTrustedString(checkBox.toString()));

        }
    };

    public Cell<Boolean> getCell() {
        if (readonly) {
            return readonlyCell;
        } else {
            return cell;
        }
    }

    public FieldUpdater<RoleProxy, Boolean> getFieldUpdater() {
        if (readonly) {
            return null;
        } else {
            return new FieldUpdater<RoleProxy, Boolean>() {

                @Override
                public void update(int index, RoleProxy role, Boolean checked) {
                    if (checked) {
                        rolesSet.add(role);
                    } else {
                        remove(role);
                    }
                }
            };
        }
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    public Boolean getValue(RoleProxy role) {
        return contains(role);
    }

    public Set<RoleProxy> getRolesSet() {
        return rolesSet;
    }

    public void setRolesSet(Set<RoleProxy> rolesSet) {
        this.rolesSet.clear();
        if (rolesSet != null) {
            this.rolesSet.addAll(rolesSet);
        }
    }

    private void remove(RoleProxy role) {
        for (RoleProxy roleProxy : rolesSet) {
            if (roleProxy.getId().equals(role.getId())) {
                rolesSet.remove(roleProxy);
                return;
            }
        }
    }

    private boolean contains(RoleProxy role) {
        for (RoleProxy roleProxy : rolesSet) {
            if (roleProxy.getId().equals(role.getId())) {
                return true;
            }
        }
        return false;
    }
}

class RoleNameCell implements HasCell<RoleProxy, RoleProxy> {

    private Cell<RoleProxy> roleNameCell = new AbstractCell<RoleProxy>() {
        @Override
        public void render(com.google.gwt.cell.client.Cell.Context context, RoleProxy value, SafeHtmlBuilder sb) {
            sb.append(SafeHtmlUtils.fromString(value.getName()));
        }
    };

    public Cell<RoleProxy> getCell() {
        return roleNameCell;
    }

    public FieldUpdater<RoleProxy, RoleProxy> getFieldUpdater() {
        return null;
    }

    public RoleProxy getValue(RoleProxy object) {
        return object;
    }
}