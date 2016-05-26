package net.amarantha.lightboard.scene;

import com.google.inject.Inject;
import net.amarantha.lightboard.updater.transport.TubeUpdater;
import net.amarantha.lightboard.utility.Sync;

public class MockTubeUpdater extends TubeUpdater {

    @Inject
    public MockTubeUpdater(Sync sync) {
        super(sync);
    }

    @Override
    protected String doTflCall() {
        return "<ArrayOfLineStatus>"
        + "<LineStatus ID=\"0\" StatusDetails=\"\">"
        + " <BranchDisruptions/>"
        + "     <Line ID=\"1\" Name=\"Bakerloo\"/>"
        + "     <Status ID=\"GS\" CssClass=\"GoodService\" Description=\"Good Service\" IsActive=\"true\">"
        + "     <StatusType ID=\"1\" Description=\"Line\"/>"
        + "     </Status>"
        + "</LineStatus>"
        + "</ArrayOfLineStatus>";
    }
}
