import javax.baja.naming.*;      /* baja User Defined*/
import com.tridium.data.*;       /* baja User Defined*/
import javax.baja.collection.*;  /* baja User Defined*/
import javax.baja.control.*;     /* control-rt User Defined*/
import javax.baja.control.ext.*; /* control-rt User Defined*/

public void onStart() throws Exception
{
  // Start up code here
}

public void onExecute() throws Exception
{
  
  // Construct ORD that looks for column named ZoneDescription for proxyExt of relevant Niagara points
  BOrd points = BOrd.make("station:|slot:/Drivers/NiagaraNetwork/FloorX/points|bql:select from control:AbstractProxyExt where name = 'proxyExt'");
  
  // Resolve the query to get the table
  BITable points_table = (BITable)points.resolve().get();
  
  // Setup cursor that goes over each row
  Cursor c = (TableCursor)points_table.cursor();
  
  while (c.next())
  {

    // Retrieve the cell value
    BAbstractProxyExt pointProxyExt = (BAbstractProxyExt) c.get();
    
    // Cast to string
    BString pointID  = (BString) pointProxyExt.get("pointId");
    
    if (pointID != null) {
      System.out.println("Current value: " + pointID);
      
      BString newPointID = BString.make(pointID.getString().replace("_X_", "_Y_"));
      
      System.out.println("New value: " + newPointID);
      
      // Invoke the set action on the point
      pointProxyExt.set("pointId", newPointID, null);     
      
    }

  }
  
  
}


public void onStop() throws Exception
{
  // shutdown code here
}

