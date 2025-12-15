package luckyfishingrod.client;

import net.minecraft.client.resources.model.BakedModel;

public final class ModelOverride {
    public static final ModelOverride INSTANCE = new ModelOverride();
    private BakedModel normal;
    private BakedModel cast;
    public void setNormal(BakedModel model) { this.normal = model; }
    public void setCast(BakedModel model)   { this.cast = model;   }
    public BakedModel getNormal() { return normal; }
    public BakedModel getCast()   { return cast;   }
}