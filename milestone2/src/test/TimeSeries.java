package test;


import java.io.*;
import java.util.*;

public class TimeSeries {
	private static final String COMMA_DELIMITER = ",";
	Scanner reader;
	Vector<Vector<Float>> record;
	Vector<String> features;

	public TimeSeries(String csvFileName)  {
		try {
			reader = new Scanner(new BufferedReader(new FileReader(csvFileName)));
			record = new Vector<>();
			String[] res = reader.nextLine().split(COMMA_DELIMITER);
			features = new Vector<>();
			features.addAll(Arrays.asList(res));
			while (reader.hasNextLine()){
			 	record.add(getRecordFromLine(reader.nextLine()));
			 }
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	private Vector<Float> getRecordFromLine(String line) {
		Vector<Float> values = new Vector<>();
		String[] res = line.split(COMMA_DELIMITER);
		for (String str: res){
			values.add(Float.parseFloat(str));
		}
		return values;
	}

	public void vectorAtIndex(int index, float[] x){
		for (int i = 0; i < this.record.size(); i++){
			x[i] = this.record.elementAt(i).elementAt(index);
		}
	}

	public Vector<Vector<Float>> getRecord() {
		return record;
	}

	public Vector<String> getFeatures() {
		return features;
	}

	public String featureAtIndex(int index){
		return this.features.elementAt(index);
	}
	public int featureByValue(String a){
		return this.features.indexOf(a);
	}
	public Float valAtIndex(int i, int j){
		return record.elementAt(i).elementAt(j);
	}

}
