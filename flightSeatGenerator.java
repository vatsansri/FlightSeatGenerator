package sampaletest;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class flightSeatGenerator {

	public static void main(String[] args) throws JSONException, IOException {
		
		int seatingStruct = 4,passengerCount=30;  // Number of seating Containers, Number of Passengers
		int row =0,column=0,rowcount=0;
		char alphabet = 'A';  // seat Container name
		JSONArray seatArrdetails = new JSONArray();
		JSONArray rowsortseatArrdetails = new JSONArray();
		JSONArray sortseatArrdetails = new JSONArray();
		String[] seatTypeOrder = {"aisle","window","middle"}; // seat order
		String seatingLimits = "[{\"row\":2,\"column\":3},{\"row\":3,\"column\":4},{\"row\":3,\"column\":2},{\"row\":4,\"column\":3}]"; // should be equal to seatingStruct		
		// Identify Seat type, row and Assign seat name
		JSONArray seatArr = new JSONArray(seatingLimits);
		for(int i =0; i< seatArr.length(); i++) {
			JSONObject seatlimitVals = seatArr.getJSONObject(i);
			row = seatlimitVals.optInt("row");
			column = seatlimitVals.optInt("column");
			if(row > rowcount) rowcount =row;
			JSONArray seatArrVals = new JSONArray();
			seatArrVals = identifySeats(row,column,i,seatingStruct,alphabet++);
			for (int j = 0; j < seatArrVals.length(); j++) {
				seatArrdetails.put(seatArrVals.getJSONObject(j));
			}
		}
		
		//Get Seats in row order from first row to last row
		for(int k=0; k < rowcount;k++) {
			JSONArray rowseatArrdetails = new JSONArray();
			rowseatArrdetails = getRowSeatDetails("Row"+(k+1),seatArrdetails);
			for (int j = 0; j < rowseatArrdetails.length(); j++) {
				rowsortseatArrdetails.put(rowseatArrdetails.getJSONObject(j));
			}
		}
		// Arrange seats in aisle,window,middle order.
		for(int i=0; i < seatTypeOrder.length; i++) {
			JSONArray rowseatArrdetails = new JSONArray();
			rowseatArrdetails = getSeatTypeDetails(seatTypeOrder[i],rowsortseatArrdetails);
			for (int j = 0; j < rowseatArrdetails.length(); j++) {
				sortseatArrdetails.put(rowseatArrdetails.getJSONObject(j));
			}
		}
		// Assign seats to passengers.
		for(int i = 0;i < passengerCount;i++) {
			if(i < sortseatArrdetails.length() ) {
			JSONObject seatjson = sortseatArrdetails.getJSONObject(i);
			if(seatjson.has("seatname"))
			seatjson.put("passenger", "Passenger"+ (i+1));
			}
		}
		alphabet = 'A';
		int posCnt=0;
		System.out.println("------------------------------------------------------------");
		System.out.println("Flight Seat Allocation");
		System.out.println("------------------------------------------------------------");

		//Put passengers in their allocated seats. Empty seats are printed.
		for(int i =0; i<  seatArr.length(); i++) {
			JSONObject seatlimitVals = seatArr.getJSONObject(i);
			row = seatlimitVals.optInt("row");
			column = seatlimitVals.optInt("column");
			for(int j=0; j<row;j++) {
				for(int k=0; k<column; k++) {
					String seatCheck = alphabet+""+(++posCnt);
					for(int l=0;l<sortseatArrdetails.length();l++) {
						JSONObject seatjson = sortseatArrdetails.getJSONObject(l);
						String seatname = seatjson.getString("seatname");
						if(seatname.contentEquals(seatCheck)) { 
							if(seatjson.has("passenger"))
							System.out.print(seatjson.getString("passenger")+"\t");
							else
							System.out.print("empty     "+"\t");	
						}
						
					}
					
				}
				System.out.println("");
				}
			System.out.println("------------------------------------------------------------");
			alphabet++;
			posCnt=0;
			}
		
		/*for(int i = 0;i < passengerCount;i++) {
			JSONObject seatjson = sortseatArrdetails.getJSONObject(i);
			System.out.println(seatjson.getString("row")+"|"+seatjson.getString("seatType")+"|"+seatjson.getString("seatname")+"|"+seatjson.getString("passenger"));

		}*/
		
	}

	private static JSONArray getSeatTypeDetails(String string, JSONArray rowsortseatArrdetails) throws JSONException {
		JSONArray rowseatArrdetails = new JSONArray();
		for(int i=0 ;i < rowsortseatArrdetails.length();i++) {
			JSONObject seatjson = rowsortseatArrdetails.getJSONObject(i);
			String row = seatjson.getString("seatType");
			if(row.equals(string)) {
				rowseatArrdetails.put(seatjson);
			}				
		}
		return rowseatArrdetails;
	}

	public static JSONArray getRowSeatDetails(String string, JSONArray seatArrdetails) throws JSONException {
	
		JSONArray rowseatArrdetails = new JSONArray();
		for(int i=0 ;i < seatArrdetails.length();i++) {
			JSONObject seatjson = seatArrdetails.getJSONObject(i);
			String row = seatjson.getString("row");
			if(row.equals(string)) {
				rowseatArrdetails.put(seatjson);
			}				
		}
		return rowseatArrdetails;
		
	}

	public static JSONArray identifySeats(int row, int column, int seatContainer,int seatingStruct,char alphabet) throws JSONException {
		
		int cnt =0;
		JSONArray seatArrdetails = new JSONArray();
		for(int i =0; i < row; i++) {
			for(int j=0; j<column;j++) {
				JSONObject seatDetails = new JSONObject();
				if((j==0 && seatContainer == 0) || (j==column-1 && seatContainer == seatingStruct-1)) {
					seatDetails.put("seatType","window"); 		     		
				}
				else if((seatContainer == 0 && j== column-1) || (j==0 && seatContainer !=0 && seatContainer != seatingStruct-1) || (j== column-1 && seatContainer !=0 && seatContainer != seatingStruct-1) ||( j==0 && seatContainer ==  seatingStruct-1)){
					seatDetails.put("seatType","aisle");		     		
				}
				else {
					seatDetails.put("seatType","middle");
		     		
				}
				seatDetails.put("row", "Row"+(i+1));
				seatDetails.put("seatname", (alphabet)+""+(++cnt));
				seatArrdetails.put(seatDetails);
	 		}
		}
		
		return seatArrdetails;
	}

}
