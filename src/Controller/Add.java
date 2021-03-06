
package Controller;

import Model.Area;
import Model.Itinary;
import Model.LoadingException;
import org.graphstream.graph.Node;


public class Add extends Command {

    private Itinary mItinary;
    private String cliendId;
    
    /**
     *
     * @param area
     * @param selectedNode
     * @param itinary
     * @param clientId
     */
    public Add(Area area, Node selectedNode, int itinary, String clientId) {
        super(area, selectedNode);
        mItinary = mArea.getTour().get(itinary);
        cliendId = clientId;
    }
    
    /**
     *
     * @throws Area.NoTourException
     */
    @Override
    public void execute() throws Area.NoTourException, LoadingException {
        add(mItinary, cliendId);
    }
    
    /**
     * 
     * @throws Area.NoTourException
     */
    @Override
    public void reverse() throws Area.NoTourException {
         deleteDelivery();
    }
    
}
