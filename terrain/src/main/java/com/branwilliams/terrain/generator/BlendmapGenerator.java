package com.branwilliams.terrain.generator;

import com.branwilliams.bundi.engine.texture.TextureData;

import java.util.function.BiFunction;

/**
 * @author Brandon
 * @since September 08, 2019
 */
public interface BlendmapGenerator {

    TextureData generateBlendmap(BiFunction<Integer, Integer, Float> heightGenerator, int width, int height);
}
