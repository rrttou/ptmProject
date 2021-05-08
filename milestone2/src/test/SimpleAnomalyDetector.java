package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.function.Consumer;

public class SimpleAnomalyDetector implements TimeSeriesAnomalyDetector {
	private final float CORR_DETECTOR = (float) 0.9;
	private float th; //thresh hold per correlatedFeatures
	private List<AnomalyReport> anomalyReportVector;
	private List<CorrelatedFeatures> correlatedFeatures;
	private CorrelatedFeatures cf;
	private  Point[] p_arr;
	@Override
	public void learnNormal(TimeSeries ts) {
		float[] x = new float[ts.getRecord().size()];
		float[] y = new float[ts.getRecord().size()];
		correlatedFeatures = new Vector<>();
		for (int i = 0; i < ts.getFeatures().size() - 1; i++){
			 ts.vectorAtIndex(i, x);
			for (int j = i + 1; j < ts.getFeatures().size(); j++){
				ts.vectorAtIndex(j, y);
				if(Math.abs(StatLib.pearson(x, y)) >= CORR_DETECTOR){
					p_arr = getP_Arr(x, y, ts);
					Line line = StatLib.linear_reg(p_arr);
					this.th = maxThreshHold(p_arr, line);
					cf = new CorrelatedFeatures(ts.featureAtIndex(i), ts.featureAtIndex(j),
							Math.abs(StatLib.pearson(x, y)), line, this.th);
					this.correlatedFeatures.add(cf);
				}
			}
		}
	}

	@Override
	public List<AnomalyReport> detect(TimeSeries ts) {
		anomalyReportVector = new ArrayList<>();
		float[] x = new float[ts.record.size()];
		float[] y = new float[ts.record.size()];
		Point[] p_arr = new Point[ts.record.size()];
		for (CorrelatedFeatures cs: this.correlatedFeatures){
			ts.vectorAtIndex(ts.featureByValue(cs.feature1), x);
			ts.vectorAtIndex(ts.featureByValue(cs.feature2), y);
			p_arr = getP_Arr(x, y, ts);
			Line line = StatLib.linear_reg(p_arr);
			int count = 1;
			for (Point p : p_arr){
				if (StatLib.dev(p, line) > cs.threshold){
					AnomalyReport a = new AnomalyReport(cs.feature1+"-"+cs.feature2, count);
					anomalyReportVector.add(a);
				}
				count++;
			}
		}
		return anomalyReportVector;
	}
	
	public List<CorrelatedFeatures> getNormalModel(){
		return this.correlatedFeatures;
	}
	public Point[] getP_Arr(float[] x, float[] y, TimeSeries ts){
		Point[] arr = new Point[ts.getRecord().size() - 1];
		for (int k = 0; k < arr.length; k++){
			arr[k] = new Point(x[k], y[k]);
		}
		return arr;
	}
	public float maxThreshHold(Point[] points, Line line){
		float max = 0;
		for (Point p: points){
			if (max < StatLib.dev(p, line))
				max = StatLib.dev(p, line);
		}
		return (float) (max*1.1);
	}
}
