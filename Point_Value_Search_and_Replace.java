public void onStart() throws Exception
{
  // Start up code here
}

public void onExecute() throws Exception
{

  // construct ORD that looks for point named ZoneDescription in any Point folder where the parent name follows some specified pattern
  BOrd interiors = BOrd.make("station:|slot:/Drivers/BacnetNetwork|bql:select ZoneDescription from bacnet:BacnetPointDeviceExt where parent.name like 'VAV%'");
  
  // Resolve the query to get the table
  BITable interiors_controllers_table = (BITable)interiors.resolve().get();
   
  // Get a list of the columns
  ColumnList interiors_columns = interiors_controllers_table.getColumns();
  
  // Isolate desired column
  Column zone_description      = interiors_columns.get("ZoneDescription");
  
  // Setup cursor that goes over each row
  TableCursor c = (TableCursor)interiors_controllers_table.cursor();
  
  while (c.next())
  {
    // NOTE: TableCursor cell() function returns a BObject object which can be type-cast to a string
    
    // check if the BObject return is not null
    if (c.cell(zone_description).toString() != "NULL") {
    
      // Retrieve the cell value (which is the ZoneDescription point itself in our case) and cast to StringWritable (this is also the type of the point ZoneDescription as seen in AX Slot Sheet)
      BStringWritable zoneDescription = ((BStringWritable)c.cell(zone_description));
      
      //String zoneDescriptionValueStatus = zoneDescription.getStatus().isOk()
      
      System.out.println("Current value: " + zoneDescription.getOut().getValue());
      
      // Invoke the set action on the point ZoneDescription
      zoneDescription.set(BString.make(zoneDescription.getOut().getValue().replace("-18-", "-02-")));
      
    }

  }
  
  
}


public void onStop() throws Exception
{
  // shutdown code here
}

