package org.excellent.client.utils.pathfinding;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.minecraft.util.math.vector.Vector3d;

@Data
@AllArgsConstructor
public class Vec3 {
    private double x;
    private double y;
    private double z;

    public Vec3 addVector(final double x, final double y2, final double z) {
        return new Vec3(this.x + x, this.y + y2, this.z + z);
    }

    public Vec3 floor() {
        return new Vec3(Math.floor(this.x), Math.floor(this.y), Math.floor(this.z));
    }

    public double squareDistanceTo(final Vec3 vector) {
        return Math.pow(vector.x - this.x, 2.0) + Math.pow(vector.y - this.y, 2.0) + Math.pow(vector.z - this.z, 2.0);
    }

    public Vec3 add(final Vec3 vector) {
        return this.addVector(vector.getX(), vector.getY(), vector.getZ());
    }

    public Vector3d mc() {
        return new Vector3d(this.x, this.y, this.z);
    }

    @Override
    public String toString() {
        return "[" + this.x + ";" + this.y + ";" + this.z + "]";
    }
}