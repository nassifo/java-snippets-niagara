public void onStart() throws Exception
{
  // Start up code here
  
}

public void onExecute() throws Exception
{

  // Accumulated values
  double Total_Cold_Interior = 0d;
  double Total_Hot_Interior  = 0d;
  
  double Total_Cold_Exterior = 0d;
  double Total_Hot_Exterior  = 0d;
  
  // Calculate Total Airflow For Interior Boxes
  BOrd interiors = BOrd.make("____|bql:select BoxFlow, HotDeckDamperModulation, ColdDeckDamperModulation from niagaraDriver:NiagaraPointFolder where name like 'MX%'");
  
  // Resolve the query to get the table
  BITable interiors_controllers_table = (BITable)interiors.resolve().get();
   
  // Get a list of the columns
  ColumnList interiors_columns = interiors_controllers_table.getColumns();
  
  Column interiors_boxflow     = interiors_columns.get("BoxFlow");
  Column interiors_hotdeckmod  = interiors_columns.get("HotDeckDamperModulation");
  Column interiors_colddeckmod = interiors_columns.get("ColdDeckDamperModulation");
  
  TableCursor c = (TableCursor)interiors_controllers_table.cursor();
  
  while (c.next())
  {
    double boxFlow     = ((BINumeric)c.cell(interiors_boxflow)).getNumeric();
    double hotDeckMod  = ((BINumeric)c.cell(interiors_hotdeckmod)).getNumeric();
    double coldDeckMod = ((BINumeric)c.cell(interiors_colddeckmod)).getNumeric();
    
    double temp_sum = ( (coldDeckMod + hotDeckMod) > 1) ? coldDeckMod + hotDeckMod : 1;
    
    Total_Cold_Interior += boxFlow*(coldDeckMod/temp_sum);
    Total_Hot_Interior  += boxFlow*(hotDeckMod/temp_sum);
  }
  
  
  // Calculate Total AirFlow For Exterior Boxes (From Below Floor)
  BOrd exteriors = BOrd.make("___|bql:select BoxFlow, HotDeckDamperModulation, ColdDeckDamperModulation from niagaraDriver:NiagaraPointFolder where name like 'MX%'");
  
  // Resolve the query to get the table
  BITable exteriors_controllers_table = (BITable)exteriors.resolve().get();
  
  // Get a list of the columns
  ColumnList exteriors_columns = exteriors_controllers_table.getColumns();
  
  Column exteriors_boxflow     = exteriors_columns.get("BoxFlow");
  Column exteriors_hotdeckmod  = exteriors_columns.get("HotDeckDamperModulation");
  Column exteriors_colddeckmod = exteriors_columns.get("ColdDeckDamperModulation");
  
  c = (TableCursor)exteriors_controllers_table.cursor();
  
  while (c.next())
  {
    double boxFlow     = ((BINumeric)c.cell(exteriors_boxflow)).getNumeric();
    double hotDeckMod  = ((BINumeric)c.cell(exteriors_hotdeckmod)).getNumeric();
    double coldDeckMod = ((BINumeric)c.cell(exteriors_colddeckmod)).getNumeric();
    
    double temp_sum = ( (coldDeckMod + hotDeckMod) > 1) ? coldDeckMod + hotDeckMod : 1;
    
    Total_Cold_Exterior += boxFlow*(coldDeckMod/temp_sum);
    Total_Hot_Exterior  += boxFlow*(hotDeckMod/temp_sum);
  }
  
  
  // Set output slot values
  setTotalColdDeckInterior( new BStatusNumeric( Total_Cold_Interior ) );
  setTotalHotDeckInterior( new BStatusNumeric( Total_Hot_Interior ) );
  
  setTotalColdDeckExterior( new BStatusNumeric( Total_Cold_Exterior ) );
  setTotalHotDeckExterior( new BStatusNumeric( Total_Hot_Exterior ) );
}


public void onStop() throws Exception
{
  // shutdown code here
}

