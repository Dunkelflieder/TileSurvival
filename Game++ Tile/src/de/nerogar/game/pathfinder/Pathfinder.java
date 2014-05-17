package de.nerogar.game.pathfinder;

import java.util.ArrayList;

import de.nerogar.game.Map;
import de.nerogar.game.Position;

public class Pathfinder {

	private static final int MAX_SEARCH_DEPTH = 10000;
	private static Node[][] nodes;
	public static boolean init = false;

	public static void init(Map map) {
		int size = map.getSize();
		nodes = new Node[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				nodes[i][j] = new Node(map.getTileAt(i, j).cost, null, new Position(i, j));
			}
		}
		init = true;
	}

	private static Node getNodeAt(Position pos) {
		if (pos.getX() < 0 || pos.getY() < 0 || pos.getX() >= nodes.length || pos.getY() >= nodes.length) return null;
		return nodes[pos.getX()][pos.getY()];
	}

	public static void reset() {
		for (int i = 0; i < nodes.length; i++) {
			for (int j = 0; j < nodes.length; j++) {
				nodes[i][j].setState(Node.STATE_INIT);
				nodes[i][j].setPointer(null);
				nodes[i][j].setDiagonal(false);
			}
		}
	}

	public static Node getPath(Map map, Position from, Position goal) {

		reset();

		ArrayList<Node> openList = new ArrayList<Node>();

		Node current = getNodeAt(from);
		current.setState(Node.STATE_OPEN);
		openList.add(current);

		// check if start and goal are valid
		Node to = getNodeAt(goal);
		if (current == null || to == null) return null;
		if (current.cost < 0 || to.cost < 0) return null;

		// try finding a path, but abort if it's too expensive (thats the limit for)
		for (int i = 0; i < MAX_SEARCH_DEPTH; i++) {
			// no path possible
			if (openList.size() == 0) return null;

			// cheapest open node is always at 0
			current = openList.get(0);

			Position[] newPos = new Position[8];
			newPos[0] = new Position(current.position.getX(), current.position.getY() - 1); // up
			newPos[1] = new Position(current.position.getX() + 1, current.position.getY()); // right
			newPos[2] = new Position(current.position.getX(), current.position.getY() + 1); // down
			newPos[3] = new Position(current.position.getX() - 1, current.position.getY()); // left
			newPos[4] = new Position(current.position.getX() + 1, current.position.getY() - 1); // up-right
			newPos[5] = new Position(current.position.getX() + 1, current.position.getY() + 1); // right-down
			newPos[6] = new Position(current.position.getX() - 1, current.position.getY() + 1); // down-left
			newPos[7] = new Position(current.position.getX() - 1, current.position.getY() - 1); // left-up

			// remember if the nodes up, right, down and left were walkable (initialized with false)
			boolean[] walkable = new boolean[4];
			for (int j = 0; j < newPos.length; j++) {

				// skip the diagonal nodes if the 2 corresponding straight nodes were not walkable
				if (j > 3 && !(walkable[j - 4] && walkable[j == 7 ? 0 : j - 3])) continue;

				Position pos = newPos[j];
				Node node = getNodeAt(pos);

				// no such node OR node already in open or closed list OR node not walkable
				if (node == null || node.getState() > Node.STATE_INIT || node.cost < 0) continue;

				node.setPointer(current);
				if (j > 3) node.setDiagonal(true);

				// goal reached
				if (pos.equals(goal)) return node;

				// add to openList at the correct place (openList stays sorted)
				addNodeSorted(openList, node, goal);
				node.setState(Node.STATE_OPEN);
				
				if (j < 4) walkable[j] = true;
			}

			// remove processed node from open list and add to pseudo closed list
			current.setState(Node.STATE_CLOSED);
			openList.remove(0);

		}

		return null;
	}

	private static void addNodeSorted(ArrayList<Node> list, Node node, Position goal) {
		int il = 0, ir = list.size();
		int mid;
		float nodecost = node.getTotalCost(goal);
		while (il < ir) {
			mid = (il + ir) / 2;
			float tempcost = list.get(mid).getTotalCost(goal);
			if (nodecost < tempcost)
				ir = mid;
			else
				il = mid + 1;
		}
		list.add(il, node);
	}

}
