package cn.iwuliao.ds.core.yml;

import java.util.*;

/**
 * @author Kris De Volder
 */
public class YamlPath {

	public static final YamlPath EMPTY = new YamlPath();
	private final YamlPathSegment[] segments;

	public YamlPath(List<YamlPathSegment> segments) {
		this.segments = segments.toArray(new YamlPathSegment[segments.size()]);
	}

	public YamlPath() {
		this.segments = new YamlPathSegment[0];
	}

	public YamlPath(YamlPathSegment... segments) {
		this.segments = segments;
	}

	public String toPropString() {
		StringBuilder buf = new StringBuilder();
		boolean first = true;
		for (YamlPathSegment s : segments) {
			if (first) {
				buf.append(s.toPropString());
			} else {
				buf.append(s.toNavString());
			}
			first = false;
		}
		return buf.toString();
	}

	public String toNavString() {
		StringBuilder buf = new StringBuilder();
		for (YamlPathSegment s : segments) {
			buf.append(s.toNavString());
		}
		return buf.toString();
	}

	public YamlPathSegment[] getSegments() {
		return segments;
	}

	/**
	 * Parse a YamlPath from a dotted property name. The segments are obtained
	 * by spliting the name at each dot.
	 */
	public static YamlPath fromProperty(String propName) {
		List<YamlPathSegment> segments = new ArrayList<>();
		String delim = ".[]";
		StringTokenizer tokens = new StringTokenizer(propName, delim, true);
		try {
			while (tokens.hasMoreTokens()) {
				String token = tokens.nextToken(delim);
				if (token.equals(".") || token.equals("]")) {
					//Skip it silently
				} else if (token.equals("[")) {
					String bracketed = tokens.nextToken("]");
					if (bracketed.equals("]")) {
						//empty string between []? Makes no sense, so ignore that.
					} else {
						try {
							int index = Integer.parseInt(bracketed);
							segments.add(YamlPathSegment.valueAt(index));
						} catch (NumberFormatException e) {
							segments.add(YamlPathSegment.valueAt(bracketed));
						}
					}
				} else {
					segments.add(YamlPathSegment.valueAt(token));
				}
			}
		} catch (NoSuchElementException e) {
			//Ran out of tokens.
		}
		return new YamlPath(segments);
	}

	/**
	 * Create a YamlPath with a single segment (i.e. like 'fromProperty', but does
	 * not parse '.' as segment separators.
	 */
	public static YamlPath fromSimpleProperty(String name) {
		return new YamlPath(YamlPathSegment.valueAt(name));
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("YamlPath(");
		boolean first = true;
		for (YamlPathSegment s : segments) {
			if (!first) {
				buf.append(", ");
			}
			buf.append(s);
			first = false;
		}
		buf.append(")");
		return buf.toString();
	}

	public int size() {
		return segments.length;
	}

	public YamlPathSegment getSegment(int segment) {
		if (segment>=0 && segment<segments.length) {
			return segments[segment];
		}
		return null;
	}

	public YamlPath prepend(YamlPathSegment s) {
		YamlPathSegment[] newPath = new YamlPathSegment[segments.length+1];
		newPath[0] = s;
		System.arraycopy(segments, 0, newPath, 1, segments.length);
		return new YamlPath(newPath);
	}

	public YamlPath append(YamlPathSegment s) {
		YamlPathSegment[] newPath = Arrays.copyOf(segments, segments.length+1);
		newPath[segments.length] = s;
		return new YamlPath(newPath);
	}

//	@Override
//	public <T extends YamlNavigable<T>> Stream<T> traverseAmbiguously(T startNode) {
//		if (startNode!=null) {
//			Stream<T> result = Stream.of(startNode);
//			for (YamlPathSegment s : segments) {
//				result = result.flatMap((node) -> {
//					return node.traverseAmbiguously(s);
//				});
//			}
//			return result;
//		}
//		return Stream.empty();
//	}

	public YamlPath dropFirst(int dropCount) {
		if (dropCount>=size()) {
			return EMPTY;
		}
		if (dropCount==0) {
			return this;
		}
		YamlPathSegment[] newPath = new YamlPathSegment[segments.length-dropCount];
		for (int i = 0; i < newPath.length; i++) {
			newPath[i] = segments[i+dropCount];
		}
		return new YamlPath(newPath);
	}

	public YamlPath dropLast() {
		return dropLast(1);
	}

	public YamlPath dropLast(int dropCount) {
		if (dropCount>=size()) {
			return EMPTY;
		}
		if (dropCount==0) {
			return this;
		}
		YamlPathSegment[] newPath = new YamlPathSegment[segments.length-dropCount];
		for (int i = 0; i < newPath.length; i++) {
			newPath[i] = segments[i];
		}
		return new YamlPath(newPath);
	}


//	@Override
	public boolean isEmpty() {
		return segments.length==0;
	}

	public YamlPath tail() {
		return dropFirst(1);
	}

	/**
	 * Attempt to convert a path represented as a list of {@link NodeRef} into YamlPath.
	 * <p>
	 * Note that not all AST path can be converted into a YamlPath. Some paths in AST
	 * do not have a corresponding YamlPath. For such cases this method may return null.
	 */
//	public static YamlPath fromASTPath(List<NodeRef<?>> path) {
//		List<YamlPathSegment> segments = new ArrayList<>(path.size());
//		for (NodeRef<?> nodeRef : path) {
//			switch (nodeRef.getKind()) {
//			case ROOT:
//				RootRef rref = (RootRef) nodeRef;
//				segments.add(YamlPathSegment.valueAt(rref.getIndex()));
//				break;
//			case KEY: {
//				String key = NodeUtil.asScalar(nodeRef.get());
//				if (key==null) {
//					return null;
//				} else {
//					segments.add(YamlPathSegment.keyAt(key));
//				} }
//				break;
//			case VAL: {
//				TupleValueRef vref = (TupleValueRef) nodeRef;
//				String key = NodeUtil.asScalar(vref.getTuple().getKeyNode());
//				if (key==null) {
//					return null;
//				} else {
//					segments.add(YamlPathSegment.valueAt(key));
//				} }
//				break;
//			case SEQ:
//				SeqRef sref = ((SeqRef)nodeRef);
//				segments.add(YamlPathSegment.valueAt(sref.getIndex()));
//				break;
//			default:
//				return null;
//			}
//		}
//		return new YamlPath(segments);
//	}

	public YamlPathSegment getLastSegment() {
		if (!isEmpty()) {
			return segments[segments.length-1];
		}
		return null;
	}

	public YamlPath commonPrefix(YamlPath other) {
		ArrayList<YamlPathSegment> common = new ArrayList<>(this.size());
		for (int i = 0; i < this.size(); i++) {
			YamlPathSegment s = this.getSegment(i);
			if (s.equals(other.getSegment(i))) {
				common.add(s);
			}
		}
		return new YamlPath(common);
	}

	public boolean canEmpty() {
		//The empty path is the only one that 'canEmpty' since any step in path moves the cursor.
		return isEmpty();
	}

}