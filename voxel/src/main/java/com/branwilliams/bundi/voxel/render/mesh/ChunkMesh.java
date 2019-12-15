package com.branwilliams.bundi.voxel.render.mesh;

import com.branwilliams.bundi.engine.core.Destructible;
import com.branwilliams.bundi.engine.mesh.Mesh;
import com.branwilliams.bundi.engine.shader.Transformable;
import com.branwilliams.bundi.engine.shader.Transformation;
import com.branwilliams.bundi.engine.util.Timer;
import com.branwilliams.bundi.voxel.VoxelConstants;
import com.branwilliams.bundi.voxel.world.chunk.VoxelChunk;

/**
 * @author Brandon
 * @since August 13, 2019
 */
public class ChunkMesh implements Destructible {

    public enum MeshState {
        /** This is the state of a mesh which has not been assigned to any chunk. */
        UNASSIGNED,
        /** This is used to differentiate between player modifications making a chunk dirty vs a chunk being new. */
        REASSIGNED,
        /** The mesh has been created and should animate into the scene. */
        LOADED,
        /** The mesh has been completely unloaded and should be released to the mesh pool. */
        UNLOADED
    }

    private MeshState meshState;

    private VoxelChunk voxelChunk;

    private Transformable transformable;

    private Mesh solidMesh;

    /** Time (in ms) when this mesh state has been changed. */
    private long changeTime;

    public ChunkMesh() {
        this.meshState = MeshState.UNASSIGNED;
        this.transformable = new Transformation();
        this.solidMesh = new Mesh();
        initializeSolidMesh();
    }

    private void initializeSolidMesh() {
        int vertexCount = 8 * 6;
        this.solidMesh.bind();
        this.solidMesh.initializeAttribute(0, 3, vertexCount * 3);
        this.solidMesh.initializeAttribute(1, 2, vertexCount * 2);
        this.solidMesh.initializeAttribute(2, 3, vertexCount * 3);
        this.solidMesh.initializeAttribute(3, 3, vertexCount * 3);
        this.solidMesh.unbind();
    }

    /**
     * Invoked whenever this mesh has been reset to another chunk.
     * Updates the transformable of this chunk mesh to match the chunk position provided. This also resets the ownership
     * of this mesh, since a position update implies that it is now owned by another chunk.
     * */
    public void reassign(VoxelChunk voxelChunk) {
        this.voxelChunk = voxelChunk;
        setMeshState(MeshState.REASSIGNED);
    }

    public void unassign() {
        setMeshState(MeshState.UNASSIGNED);
    }

    public void unload() {
        this.voxelChunk = null;
        setMeshState(MeshState.UNLOADED);
    }

    public void load() {
        setMeshState(MeshState.LOADED);
    }

    public void setMeshState(MeshState meshState) {
        boolean changed = this.meshState != meshState;
        this.meshState = meshState;
        if (changed)
            onMeshChangeState(meshState);
    }

    /**
     * Invoked when the mesh of this object is created for the first time for a chunk.
     * */
    public void onMeshChangeState(MeshState meshState) {
        changeTime = Timer.getSystemTime();
    }

    /**
     * @return The transformable used to render this chunk mesh.
     * */
    public Transformable getTransformable(float animationHeight) {
        float y = -animationHeight + (getAnimation() * animationHeight);
        if (meshState == MeshState.UNASSIGNED) {
            y = (getAnimation() * -animationHeight);
            System.out.println("UNASSIGNED y= " + y);
        }

        return transformable.position(voxelChunk.chunkPos.getRealX(), y, voxelChunk.chunkPos.getRealZ());
    }

    /**
     * @return A value from 0 ~ 1 representing the time between this meshes creation to the current time.
     * */
    public float getAnimation() {
        float animation = (float) (Timer.getSystemTime() - changeTime) / (float) VoxelConstants.CHUNK_ANIMATION_TIME_MS;
        animation = Math.min(1F, animation);
//        if (animation != 1F)
//            System.out.println("meshState=" + meshState.name() + ", animation=" + animation);
//        if (meshState == MeshState.LOADED) {
//            return animation;
//        } else if (meshState == MeshState.UNASSIGNED) {
//            return Math.max(0F, 1F - animation);
//        }
        return animation;
    }

    /**
     * @return True if this chunk mesh has finished animating into view.
     * */
    public boolean finishedAnimation() {
        return getAnimation() == 1F;
    }

    /**
     *
     * */
    public boolean isRenderable() {
        return meshState == MeshState.LOADED || meshState == MeshState.UNASSIGNED;
    }

    public MeshState getMeshState() {
        return meshState;
    }

    public Mesh getSolidMesh() {
        return solidMesh;
    }

    public VoxelChunk getVoxelChunk() {
        return voxelChunk;
    }

    @Override
    public void destroy() {
        solidMesh.destroy();
    }

}
