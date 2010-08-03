/*
 * This program is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software Foundation;
 * either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program;
 * if not, see <http://www.gnu.org/licenses/>.
 */

package gui;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

/**
 *
 * @author Juergen Simon
 */
public class Gui_JTTable extends JTable{
	private static final long serialVersionUID = -163847925662887231L;

	public Gui_JTTable() {
        super();
    }

    public Gui_JTTable(TableModel model) {
        super(model);
    }

    private String getCellToolTip(MouseEvent event) {
        String toolTip = null;

        int col = columnAtPoint(event.getPoint());
        int row = rowAtPoint(event.getPoint());

        if (row >= 0 && col >= 0) {
            TableCellRenderer renderer = getCellRenderer(row, col);
            Component component = prepareRenderer(renderer, row, col);

            if (component instanceof JLabel) {
                // Convert the event to the renderer's coordinate system
                Rectangle cellRect = getCellRect(row, col, false);
                if(cellRect.width < component.getPreferredSize().width) {
                    toolTip = ((JLabel)component).getText();
                }
            }
        }
        return toolTip;
    }

    @Override
    public String getToolTipText(MouseEvent event) {
        String toolTip = getCellToolTip(event);
        if (toolTip != null) {
            return toolTip;
        }
        return getToolTipText();
    }

    @Override
    public Point getToolTipLocation(MouseEvent event){

        int col = columnAtPoint( event.getPoint() );
        int row = rowAtPoint( event.getPoint() );
        if (row >= 0 && col >= 0 && getCellToolTip(event) != null) {
            return getCellRect(row, col, false).getLocation();
        }
        return super.getToolTipLocation(event);
    }
}

 	  	 
