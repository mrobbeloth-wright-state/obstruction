package robbeloth.research;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.opencv.core.Mat;
import org.opencv.core.Point;

public class LGNode {
	/* location of the node, correspondent to region's centroid 
	 * or the center of gravity */
	private Point center;  
	
	// Region stats
	private HashMap<String, Double> stats;
	
	/* Contains average of all rows, cols, and intensity for region
	   Note there are no stats for regions created by region growing
	   process where small regions are inserted within the original
	   clusters */
	public HashMap<String, Double> getStats() {
		return stats;
	}

	// local graph associated with this node (region)
	private ArrayList<CurveLineSegMetaData> L;  
	
	// object contour pixel set
	private Mat border;
	
	// number of pixels belonging to this region
	private long size;
	
	// Partitioning algorithm used
	private ProjectUtilities.Partitioning_Algorithm pa = 
			ProjectUtilities.Partitioning_Algorithm.NONE;
	
	// Node index for LG Algorithm processing
	private int node_id = -1;
	
	/**
	 * Default constructor
	 */
	public LGNode() {
		super();
		this.stats = new HashMap<String, Double>();		
		this.center = new Point(0,0);
		this.L = new ArrayList<CurveLineSegMetaData>();
		this.border = new Mat();
		this.size = 0;
	}
	
	public LGNode(Point center, Mat border, ProjectUtilities.Partitioning_Algorithm pa, 
			       int node_id) {
		this();
		
		// copy the center for the region (segment)
		if (center != null) {
			this.center = center;	
		}
		
		// copy the object contour pixel set
		this.border = border.clone();
		
		// Record the partitioning algorithm used
		if (pa != null) {
			this.pa = pa;	
		}
		
		/* total number of non-zero pixels in region */
		this.size = ProjectUtilities.DetermineNodeSize(border);
		
		/* Record region (Node) identifier*/
		this.node_id = node_id;
	}
	
	public LGNode(Point center, HashMap<String, Double> stats, 
			       Mat border, ProjectUtilities.Partitioning_Algorithm pa,
			       int node_id) {
		// call simpler constructor for node center
		this(center, border, pa, node_id);
		
		// now save stats
		this.stats = new HashMap<String, Double>(stats.size());
		Set<String> keys = stats.keySet();
		for(String key : keys) {
			Double value = stats.get(key);
			this.stats.put(key, value);
		}
	}
	/**
	 * 
	 * @param center -- Location of the node, correspondent to region's centroid 
	 * or the center of gravity
	 * @param stats -- Region statistics
	 * @param border -- Object contour pixel set
	 * @param lmd -- Metadata of all the curved line segments in a region
	 * @param pa --  Partitioning algorithm
	 * @param node_id -- Node index for LG Algorithm processing
	 */
	public LGNode(Point center, HashMap<String, Double> stats, 
			Mat border, ArrayList<CurveLineSegMetaData> lmd, 
			ProjectUtilities.Partitioning_Algorithm pa, 
			int node_id) {
		this(center, stats, border, pa, node_id);
		
		// now save local graph associated with this node/region/segment
		this.L = new ArrayList<CurveLineSegMetaData>(lmd.size());
		for(int i = 0; i < lmd.size(); i++) {
			this.L.add((lmd.get(i)));
		}
		
	}

	public Point getCenter() {
		return center;
	}

	public ArrayList<CurveLineSegMetaData> getL() {
		return L;
	}

	public Mat getBorder() {
		return border;
	}

	public long getSize() {
		return size;
	}
	
	public int getNodeID() {
		return node_id;
	}

	public void setL(ArrayList<CurveLineSegMetaData> lmd) {
		// now save local graph associated with this node/region/segment
		this.L = new ArrayList<CurveLineSegMetaData>(lmd.size());
		for(int i = 0; i < lmd.size(); i++) {
			this.L.add((lmd.get(i)));
		}
	}

	public void setBorder(Mat border) {
		this.border = border;
	}

	public void setSize(long size) {
		this.size = size;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("LGNode [center=" + center + ", stats="
				+ stats + ", L=" + L + ", border=" + border.toString() 
				+ ", size=" + size + "]\n");
		return sb.toString();
	}
		
}
