/**
 * Checks of Mission 0. Run:  java -cp out DisjointSetTest
 * Add your own checks below: a large chain, a union of a set with itself, count after many unions.
 */
public class DisjointSetTest {

    static int failed = 0;

    static void check(boolean ok, String what) {
        System.out.println((ok ? "ok    " : "FAIL  ") + what);
        if (!ok) failed++;
    }

    public static void main(String[] args) {
        DisjointSet d = new DisjointSet(6);
        check(d.count() == 6, "6 sets at the start");
        check(!d.connected(0, 1), "0 and 1 are apart at the start");
        check(d.union(0, 1), "union(0, 1) joins two sets");
        check(d.connected(0, 1), "0 and 1 are together");
        check(!d.union(1, 0), "union(1, 0) again returns false");
        check(d.count() == 5, "5 sets after one union");
        d.union(2, 3); d.union(1, 3);
        check(d.connected(0, 2), "0 and 2 are together through 1 and 3");
        check(d.count() == 3, "3 sets: {0,1,2,3} {4} {5}");
        check(d.find(4) == 4 && d.find(5) == 5, "a single cell is its own root");

        // TODO: add your own checks here
        DisjointSet chain = new DisjointSet(50);
        for (int i = 0; i < 49; i++) {
            chain.union(i, i+1);
        }

        check(chain.count() == 1, "50 elements of chain in one set");
        check(chain.connected(0, 49), "all 50 elements are connected");

        DisjointSet selfDs = new DisjointSet(5);
        check(selfDs.union(2, 2), "Union of an element with itself returns true");
        check(!selfDs.union(1, 1), "UNion of already connected elements returns false");

        selfDs.union(0, 1);
        check(!selfDs.union(0, 1), "Union of already connected elements returns false");

        DisjointSet manyDs = new DisjointSet(10);

        for (int i = 0; i < 4; i++) {
            manyDs.union(i, i+1);
        }

        for (int i = 5; i < 9; i++) {
            manyDs.union(i, i+1);
        }

        check(manyDs.count() == 2, "After creating two separate trees count should be 2");

        manyDs.union(0, 9);
        check(manyDs.count() ==1, "Count should drop to 1 after merging two sets");
        System.out.println(failed == 0 ? "All checks passed." : failed + " checks failed.");
    }
}
