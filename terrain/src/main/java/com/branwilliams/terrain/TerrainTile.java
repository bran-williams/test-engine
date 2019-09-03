package com.branwilliams.terrain;

import com.branwilliams.bundi.engine.core.Destructible;
import com.branwilliams.bundi.engine.mesh.Mesh;
import com.branwilliams.bundi.engine.shader.Material;
import com.branwilliams.bundi.engine.shader.Transformable;
import com.branwilliams.bundi.engine.shape.AABB;

/**
 * @author Brandon
 * @since August 30, 2019
 */
public class TerrainTile implements Destructible {

    private Transformable transform;

    private Mesh mesh;

    private Material material;

    private AABB collisionBox;

    private float[][] heightmap;

    public TerrainTile(float[][] heightmap, Transformable transform, Mesh mesh, Material material) {
        this.heightmap = heightmap;
        this.transform = transform;
        this.mesh = mesh;
        this.material = material;
    }

    public Transformable getTransform() {
        return transform;
    }

    public void setTransform(Transformable transform) {
        this.transform = transform;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public AABB getCollisionBox() {
        return collisionBox;
    }

    public void setCollisionBox(AABB collisionBox) {
        this.collisionBox = collisionBox;
    }

    @Override
    public void destroy() {
        this.mesh.destroy();
    }

}