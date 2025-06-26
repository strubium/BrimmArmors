package concord.loader.obj;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class Face {
    public Vec3[] vertices;
    public Vec3[] vertexNormals;
    public Vec3 faceNormal;
    public TextureCoordinate[] textureCoordinates;

    public void addFaceForRender(PoseStack poseStack, VertexConsumer buffer, int combinedLight, int combinedOverlay) {
        addFaceForRender(poseStack, buffer, 5.0E-4F, combinedLight, combinedOverlay);
    }

    public void addFaceForRender(PoseStack poseStack, VertexConsumer buffer, float textureOffset, int combinedLight, int combinedOverlay) {
        if (this.faceNormal == null) {
            this.faceNormal = this.calculateFaceNormal();
        }

        float averageU = 0.0F;
        float averageV = 0.0F;

        if (this.textureCoordinates != null && this.textureCoordinates.length > 0) {
            for (TextureCoordinate textureCoordinate : this.textureCoordinates) {
                averageU += textureCoordinate.u;
                averageV += textureCoordinate.v;
            }

            averageU /= this.textureCoordinates.length;
            averageV /= this.textureCoordinates.length;
        }

        PoseStack.Pose pose = poseStack.last();

        for (int i = 0; i < this.vertices.length; ++i) {
            float offsetU = 0.0F;
            float offsetV = 0.0F;

            if (this.textureCoordinates != null && this.textureCoordinates.length > 0) {
                offsetU = this.textureCoordinates[i].u > averageU ? -textureOffset : textureOffset;
                offsetV = this.textureCoordinates[i].v > averageV ? -textureOffset : textureOffset;

                buffer.vertex(pose.pose(), (float) this.vertices[i].x, (float) this.vertices[i].y, (float) this.vertices[i].z)
                        .color(1f, 1f, 1f, 1f)
                        .uv(this.textureCoordinates[i].u + offsetU, this.textureCoordinates[i].v + offsetV)
                        .overlayCoords(combinedOverlay)
                        .uv2(combinedLight)
                        .normal(pose.normal(), (float) this.faceNormal.x, (float) this.faceNormal.y, (float) this.faceNormal.z)
                        .endVertex();
            } else {
                buffer.vertex(pose.pose(), (float) this.vertices[i].x, (float) this.vertices[i].y, (float) this.vertices[i].z)
                        .color(1f, 1f, 1f, 1f)
                        .uv(0.0f, 0.0f)
                        .overlayCoords(combinedOverlay)
                        .uv2(combinedLight)
                        .normal(pose.normal(), (float) this.faceNormal.x, (float) this.faceNormal.y, (float) this.faceNormal.z)
                        .endVertex();
            }
        }
    }

    public Vec3 calculateFaceNormal() {
        Vec3 v1 = this.vertices[1].subtract(this.vertices[0]);
        Vec3 v2 = this.vertices[2].subtract(this.vertices[0]);
        Vec3 normalVector = v1.cross(v2).normalize();
        return normalVector;
    }
}
