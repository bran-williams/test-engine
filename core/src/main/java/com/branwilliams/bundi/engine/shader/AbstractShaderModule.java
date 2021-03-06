package com.branwilliams.bundi.engine.shader;

import com.branwilliams.bundi.engine.shader.patching.ShaderPatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractShaderModule implements ShaderModule {

    private final List<ShaderPatch> shaderPatches;

    public AbstractShaderModule() {
        shaderPatches = new ArrayList<>();
    }

    public void addShaderPatches(List<ShaderPatch> shaderPatches) {
        this.shaderPatches.addAll(shaderPatches);
    }
    @Override
    public List<ShaderPatch> getShaderPatches() {
        return shaderPatches;
    }
}
