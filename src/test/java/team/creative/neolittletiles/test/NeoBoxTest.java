package team.creative.neolittletiles.test;

import team.creative.neolittletiles.common.math.NeoBox;

/**
 * Unit tests for NeoBox class
 * Tests 3D coordinate operations without requiring Minecraft client startup
 */
public class NeoBoxTest {
    
    public static void main(String[] args) {
        runAllTests();
    }
    
    public static void runAllTests() {
        System.out.println("=== NeoBox Unit Tests ===");
        
        testBoxCreation();
        testVolumeCalculation();
        testIntersectionDetection();
        testContainmentChecking();
        testUnionOperation();
        testIntersectionCalculation();
        testValidation();
        testEqualsAndHashCode();
        
        System.out.println("All NeoBox tests completed successfully!");
    }
    
    private static void testBoxCreation() {
        System.out.println("Testing box creation...");
        
        NeoBox box = new NeoBox(0, 0, 0, 10, 10, 10);
        assert box.minX == 0 && box.minY == 0 && box.minZ == 0 : "Min coordinates should be 0";
        assert box.maxX == 10 && box.maxY == 10 && box.maxZ == 10 : "Max coordinates should be 10";
        
        // Test copy constructor
        NeoBox copy = new NeoBox(box);
        assert copy.equals(box) : "Copy should equal original";
        assert copy != box : "Copy should be different object";
        
        System.out.println("Box creation tests passed!");
    }
    
    private static void testVolumeCalculation() {
        System.out.println("Testing volume calculation...");
        
        NeoBox box1 = new NeoBox(0, 0, 0, 10, 10, 10);
        assert box1.getVolume() == 1000 : "10x10x10 box should have volume 1000";
        
        NeoBox box2 = new NeoBox(0, 0, 0, 1, 1, 1);
        assert box2.getVolume() == 1 : "1x1x1 box should have volume 1";
        
        NeoBox box3 = new NeoBox(5, 5, 5, 15, 15, 15);
        assert box3.getVolume() == 1000 : "Offset 10x10x10 box should have volume 1000";
        
        System.out.println("Volume calculation tests passed!");
    }
    
    private static void testIntersectionDetection() {
        System.out.println("Testing intersection detection...");
        
        NeoBox box1 = new NeoBox(0, 0, 0, 10, 10, 10);
        NeoBox box2 = new NeoBox(5, 5, 5, 15, 15, 15);
        NeoBox box3 = new NeoBox(20, 20, 20, 30, 30, 30);
        
        assert box1.intersects(box2) : "Overlapping boxes should intersect";
        assert box2.intersects(box1) : "Intersection should be symmetric";
        assert !box1.intersects(box3) : "Non-overlapping boxes should not intersect";
        assert !box3.intersects(box1) : "Intersection should be symmetric for non-overlapping";
        
        // Test edge cases
        NeoBox touching = new NeoBox(10, 10, 10, 20, 20, 20);
        assert !box1.intersects(touching) : "Boxes touching at edge should not intersect";
        
        System.out.println("Intersection detection tests passed!");
    }
    
    private static void testContainmentChecking() {
        System.out.println("Testing containment checking...");
        
        NeoBox outer = new NeoBox(0, 0, 0, 20, 20, 20);
        NeoBox inner = new NeoBox(5, 5, 5, 15, 15, 15);
        NeoBox partial = new NeoBox(10, 10, 10, 30, 30, 30);
        
        assert outer.contains(inner) : "Outer box should contain inner box";
        assert !inner.contains(outer) : "Inner box should not contain outer box";
        assert !outer.contains(partial) : "Box should not contain partially overlapping box";
        
        // Test self-containment
        assert outer.contains(outer) : "Box should contain itself";
        
        System.out.println("Containment checking tests passed!");
    }
    
    private static void testUnionOperation() {
        System.out.println("Testing union operation...");
        
        NeoBox box1 = new NeoBox(0, 0, 0, 10, 10, 10);
        NeoBox box2 = new NeoBox(5, 5, 5, 15, 15, 15);
        
        NeoBox original = new NeoBox(box1);
        box1.union(box2);
        
        assert box1.minX == 0 && box1.minY == 0 && box1.minZ == 0 : "Union min should be minimum of both";
        assert box1.maxX == 15 && box1.maxY == 15 && box1.maxZ == 15 : "Union max should be maximum of both";
        assert box1.contains(original) : "Union should contain original box";
        assert box1.contains(box2) : "Union should contain second box";
        
        System.out.println("Union operation tests passed!");
    }
    
    private static void testIntersectionCalculation() {
        System.out.println("Testing intersection calculation...");
        
        NeoBox box1 = new NeoBox(0, 0, 0, 10, 10, 10);
        NeoBox box2 = new NeoBox(5, 5, 5, 15, 15, 15);
        NeoBox box3 = new NeoBox(20, 20, 20, 30, 30, 30);
        
        NeoBox intersection = box1.intersection(box2);
        assert intersection != null : "Overlapping boxes should have intersection";
        assert intersection.minX == 5 && intersection.minY == 5 && intersection.minZ == 5 : "Intersection min should be correct";
        assert intersection.maxX == 10 && intersection.maxY == 10 && intersection.maxZ == 10 : "Intersection max should be correct";
        assert intersection.getVolume() == 125 : "5x5x5 intersection should have volume 125";
        
        NeoBox noIntersection = box1.intersection(box3);
        assert noIntersection == null : "Non-overlapping boxes should have no intersection";
        
        System.out.println("Intersection calculation tests passed!");
    }
    
    private static void testValidation() {
        System.out.println("Testing validation...");
        
        NeoBox valid = new NeoBox(0, 0, 0, 10, 10, 10);
        assert valid.isValid() : "Normal box should be valid";
        
        NeoBox invalid1 = new NeoBox(10, 0, 0, 5, 10, 10);
        assert !invalid1.isValid() : "Box with minX > maxX should be invalid";
        
        NeoBox invalid2 = new NeoBox(0, 10, 0, 10, 5, 10);
        assert !invalid2.isValid() : "Box with minY > maxY should be invalid";
        
        NeoBox invalid3 = new NeoBox(0, 0, 10, 10, 10, 5);
        assert !invalid3.isValid() : "Box with minZ > maxZ should be invalid";
        
        NeoBox point = new NeoBox(5, 5, 5, 5, 5, 5);
        assert !point.isValid() : "Point box should be invalid";
        
        System.out.println("Validation tests passed!");
    }
    
    private static void testEqualsAndHashCode() {
        System.out.println("Testing equals and hashCode...");
        
        NeoBox box1 = new NeoBox(0, 0, 0, 10, 10, 10);
        NeoBox box2 = new NeoBox(0, 0, 0, 10, 10, 10);
        NeoBox box3 = new NeoBox(1, 0, 0, 10, 10, 10);
        
        assert box1.equals(box2) : "Identical boxes should be equal";
        assert box2.equals(box1) : "Equality should be symmetric";
        assert !box1.equals(box3) : "Different boxes should not be equal";
        assert box1.equals(box1) : "Box should equal itself";
        assert !box1.equals(null) : "Box should not equal null";
        assert !box1.equals("not a box") : "Box should not equal non-box object";
        
        // Test hashCode contract
        assert box1.hashCode() == box2.hashCode() : "Equal boxes should have same hash code";
        
        System.out.println("Equals and hashCode tests passed!");
    }
}