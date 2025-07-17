package interactive.blackout.brimm.loader.obj;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class Face {
    public Vector3f[] vertices;
    public Vector3f[] vertexNormals;
    public Vector3f faceNormal;
    public TextureCoordinate[] textureCoordinates;

    @OnlyIn(Dist.CLIENT)
    public void addFaceForRender(Tessellator tessellator) {
        this.addFaceForRender(tessellator, 5.0E-4F);
    }

    @OnlyIn(Dist.CLIENT)
    public void addFaceForRender(MatrixStack matrix, IVertexBuilder buffer, int combinedLight, int combinedOverlay) {
        this.addFaceForRender(matrix, buffer, 5.0E-4F, combinedLight, combinedOverlay);
    }

    @OnlyIn(Dist.CLIENT)
    public void addFaceForRender(BufferBuilder builder, MatrixStack matrix) {
        this.addFaceForRender(builder, matrix, 5.0E-4F);
    }

    @OnlyIn(Dist.CLIENT)
    public void addFaceForRender(BufferBuilder buffer, MatrixStack matrixStack, float textureOffset) {
        if (this.faceNormal == null) {
            this.faceNormal = this.calculateFaceNormal();
        }

        float averageU = 0.0F;
        float averageV = 0.0F;
        int i;
        if (this.textureCoordinates != null && this.textureCoordinates.length > 0) {
            TextureCoordinate[] var6 = this.textureCoordinates;
            int var7 = var6.length;

            for (i = 0; i < var7; ++i) {
                TextureCoordinate textureCoordinate = var6[i];
                averageU += textureCoordinate.u;
                averageV += textureCoordinate.v;
            }

            averageU /= (float) this.textureCoordinates.length;
            averageV /= (float) this.textureCoordinates.length;
        }

        for (i = 0; i < this.vertices.length; ++i) {
            if (this.textureCoordinates != null && this.textureCoordinates.length > 0) {
                float offsetU = textureOffset;
                float offsetV = textureOffset;
                if (this.textureCoordinates[i].u > averageU) {
                    offsetU = -textureOffset;
                }

                if (this.textureCoordinates[i].v > averageV) {
                    offsetV = -textureOffset;
                }

                buffer.vertex(matrixStack.last().pose(), this.vertices[i].x(), this.vertices[i].y(), this.vertices[i].z())
                        .uv(this.textureCoordinates[i].u + offsetU, this.textureCoordinates[i].v + offsetV)
                        .normal(this.faceNormal.x(), this.faceNormal.y(), this.faceNormal.z())
                        .endVertex();
            } else {
                buffer.vertex(matrixStack.last().pose(), this.vertices[i].x(), this.vertices[i].y(), this.vertices[i].z())
                        .uv(0.0f, 0.0f)
                        .normal(this.faceNormal.x(), this.faceNormal.y(), this.faceNormal.z())
                        .endVertex();
            }
        }

    }

    @OnlyIn(Dist.CLIENT)
    public void addFaceForRender(Tessellator tessellator, float textureOffset) {
        BufferBuilder buffer = tessellator.getBuilder();
        if (this.faceNormal == null) {
            this.faceNormal = this.calculateFaceNormal();
        }

        float averageU = 0.0F;
        float averageV = 0.0F;
        int i;
        if (this.textureCoordinates != null && this.textureCoordinates.length > 0) {
            TextureCoordinate[] var6 = this.textureCoordinates;
            int var7 = var6.length;

            for (i = 0; i < var7; ++i) {
                TextureCoordinate textureCoordinate = var6[i];
                averageU += textureCoordinate.u;
                averageV += textureCoordinate.v;
            }

            averageU /= (float) this.textureCoordinates.length;
            averageV /= (float) this.textureCoordinates.length;
        }

        for (i = 0; i < this.vertices.length; ++i) {
            if (this.textureCoordinates != null && this.textureCoordinates.length > 0) {
                float offsetU = textureOffset;
                float offsetV = textureOffset;
                if (this.textureCoordinates[i].u > averageU) {
                    offsetU = -textureOffset;
                }

                if (this.textureCoordinates[i].v > averageV) {
                    offsetV = -textureOffset;
                }

                buffer.vertex(this.vertices[i].x(), this.vertices[i].y(), this.vertices[i].z())
                        .uv(this.textureCoordinates[i].u + offsetU, this.textureCoordinates[i].v + offsetV)
                        .color(1f, 1f, 1f, 1f)
                        .normal(this.faceNormal.x(), this.faceNormal.y(), this.faceNormal.z())
                        .endVertex();
            } else {
                buffer.vertex(this.vertices[i].x(), this.vertices[i].y(), this.vertices[i].z())
                        .uv(0.0f, 0.0f)
                        .color(1f, 1f, 1f, 1f)
                        .normal(this.faceNormal.x(), this.faceNormal.y(), this.faceNormal.z())
                        .endVertex();
            }
        }

    }
    @OnlyIn(Dist.CLIENT)
    public void addFaceForRender(MatrixStack matrix, IVertexBuilder buffer, float textureOffset, int combinedLight, int combinedOverlay) {
        if (this.faceNormal == null) {
            this.faceNormal = this.calculateFaceNormal();
        }

        float averageU = 0.0F;
        float averageV = 0.0F;
        int i;
        if (this.textureCoordinates != null && this.textureCoordinates.length > 0) {
            TextureCoordinate[] var6 = this.textureCoordinates;
            int var7 = var6.length;

            for (i = 0; i < var7; ++i) {
                TextureCoordinate textureCoordinate = var6[i];
                averageU += textureCoordinate.u;
                averageV += textureCoordinate.v;
            }

            averageU /= (float) this.textureCoordinates.length;
            averageV /= (float) this.textureCoordinates.length;
        }

        for (i = 0; i < this.vertices.length; ++i) {
            if (this.textureCoordinates != null && this.textureCoordinates.length > 0) {
                float offsetU = textureOffset;
                float offsetV = textureOffset;
                if (this.textureCoordinates[i].u > averageU) {
                    offsetU = -textureOffset;
                }

                if (this.textureCoordinates[i].v > averageV) {
                    offsetV = -textureOffset;
                }

                buffer.vertex(matrix.last().pose(), this.vertices[i].x(), this.vertices[i].y(), this.vertices[i].z())
                        .color(1f, 1f, 1f, 1f)
                        .uv(this.textureCoordinates[i].u + offsetU, this.textureCoordinates[i].v + offsetV)
                        .normal(this.faceNormal.x(), this.faceNormal.y(), this.faceNormal.z())
                        .uv2(combinedLight)
                        .overlayCoords(combinedOverlay)
                        .endVertex();
            } else {
                buffer.vertex(matrix.last().pose(), this.vertices[i].x(), this.vertices[i].y(), this.vertices[i].z())
                        .color(1f, 1f, 1f, 1f)
                        .uv(0.0f, 0.0f)
                        .normal(this.faceNormal.x(), this.faceNormal.y(), this.faceNormal.z())
                        .uv2(combinedLight)
                        .overlayCoords(combinedOverlay)
                        .endVertex();
            }
        }

    }

    public Vector3f calculateFaceNormal() {
        Vector3d v1 = new Vector3d(this.vertices[1].x() - this.vertices[0].x(), this.vertices[1].y() - this.vertices[0].y(), this.vertices[1].z() - this.vertices[0].z());
        Vector3d v2 = new Vector3d(this.vertices[2].x() - this.vertices[0].x(), this.vertices[2].y() - this.vertices[0].y(), this.vertices[2].z() - this.vertices[0].z());
        Vector3d normalVector = v1.cross(v2).normalize();
        return new Vector3f((float) normalVector.x, (float) normalVector.y, (float) normalVector.z);
    }
}
