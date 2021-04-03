package test;
public class StatLib {
	public static float sumBribua(float[] x){
		float sum=0;
		for (float v : x) {
			sum += Math.pow(v,2);
		}
		return sum;
	}
	// simple average
	public static float avg(float[] x){
		float sum=0;
		for (float v : x) {
			sum += v;
		}
		return sum/x.length;
	}
	// returns the variance of X and Y
	public static float var(float[] x){
		float sum=sumBribua(x)/x.length;
		sum-=Math.pow(avg(x),2);
		return sum;

	}
	// returns the covariance of X and Y
	public static float cov(float[] x, float[] y){
			if(x.length!=y.length)
				return 0;

			float ans=0;
		for (int i = 0; i <x.length ; i++) {
			ans+=((x[i]-avg(x))*(y[i]-avg(y)));
		}
		return ans/x.length;
	}
	// returns the Pearson correlation coefficient of X and Y
	public static float pearson(float[] x, float[] y){
		float y_=(float)Math.sqrt(var(y));
		float x_=(float)Math.sqrt(var(x));
		float cov=cov(x,y);
		return cov/(x_*y_);
	}
	// performs a linear regression and returns the line equation
	public static Line linear_reg(Point[] points){
		float []x=new float[points.length];
		float []y=new float[points.length];
		float a=0,b=0;
		for (int i = 0; i < points.length ; i++) {
			x[i]=points[i].x;
			y[i]=points[i].y;
		}
		a=cov(x,y)/var(x);
		b=avg(y)-a*avg(x);
		return new Line(a,b);
	}
	// returns the deviation between point p and the line equation of the points
	public static float dev(Point p,Point[] points){
		Line line=linear_reg(points);
		float ans=line.f(p.x)-p.y;
		if (ans<0)
			ans*=-1;
		return ans;
	}
	// returns the deviation between point p and the line
	public static float dev(Point p,Line l){
		float ans=l.f(p.x)-p.y;
			if(ans<0)
				ans*=-1;
			return ans;
	}
	
}
