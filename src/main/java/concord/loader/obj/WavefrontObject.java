package concord.loader.obj;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import concord.loader.IModelCustom;
import concord.loader.ModelFormatException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.phys.Vec3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;

public class WavefrontObject implements IModelCustom {
    private static final Pattern vertexPattern = Pattern.compile("(v( (\\-)?\\d+(\\.\\d+)?){3,4} *\\n)|(v( (\\-)?\\d+(\\.\\d+)?){3,4} *$)");
    private static final Pattern vertexNormalPattern = Pattern.compile("(vn( (\\-)?\\d+(\\.\\d+)?){3,4} *\\n)|(vn( (\\-)?\\d+(\\.\\d+)?){3,4} *$)");
    private static final Pattern textureCoordinatePattern = Pattern.compile("(vt( (\\-)?\\d+\\.\\d+){2,3} *\\n)|(vt( (\\-)?\\d+(\\.\\d+)?){2,3} *$)");
    private static final Pattern facePattern = Pattern.compile("(f( \\d+(/\\d+)?(/\\d+)?){3,4} *\\n)|(f( \\d+(/\\d+)?(/\\d+)?){3,4} *$)");

    private final String fileName;
    public ArrayList<Vec3> vertices = new ArrayList<>();
    public ArrayList<Vec3> vertexNormals = new ArrayList<>();
    public ArrayList<TextureCoordinate> textureCoordinates = new ArrayList<>();
    public ArrayList<GroupObject> groupObjects = new ArrayList<>();
    private GroupObject currentGroupObject;

    public WavefrontObject(ResourceLocation resource) throws ModelFormatException {
        this.fileName = resource.toString();

        try (InputStream inputStream = Minecraft.getInstance().getResourceManager().open(resource)) {
            this.loadObjModel(inputStream);
        } catch (IOException e) {
            throw new ModelFormatException("IO Exception reading model format", e);
        }
    }

    private void loadObjModel(InputStream inputStream) throws ModelFormatException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                currentLine = currentLine.trim();
                if (currentLine.isEmpty() || currentLine.startsWith("#")) continue;

                if (currentLine.startsWith("v ")) {
                    vertices.add(parseVertex(currentLine));
                } else if (currentLine.startsWith("vn ")) {
                    vertexNormals.add(parseVertexNormal(currentLine));
                } else if (currentLine.startsWith("vt ")) {
                    textureCoordinates.add(parseTextureCoordinate(currentLine));
                } else if (currentLine.startsWith("f ")) {
                    if (currentGroupObject == null) {
                        currentGroupObject = new GroupObject("Default");
                    }
                    currentGroupObject.faces.add(parseFace(currentLine));
                } else if (currentLine.startsWith("g ") || currentLine.startsWith("o ")) {
                    if (currentGroupObject != null) {
                        groupObjects.add(currentGroupObject);
                    }
                    currentGroupObject = new GroupObject(currentLine.substring(2).trim());
                }
            }

            if (currentGroupObject != null) {
                groupObjects.add(currentGroupObject);
            }
        } catch (IOException e) {
            throw new ModelFormatException("Error reading OBJ file", e);
        }

        Collections.sort(groupObjects);
    }

    private Vec3 parseVertex(String line) throws ModelFormatException {
        String[] tokens = line.substring(2).trim().split(" ");
        if (tokens.length < 3) {
            throw new ModelFormatException("Invalid vertex format");
        }
        return new Vec3(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]));
    }

    private Vec3 parseVertexNormal(String line) throws ModelFormatException {
        String[] tokens = line.substring(3).trim().split(" ");
        if (tokens.length < 3) {
            throw new ModelFormatException("Invalid vertex normal format");
        }
        return new Vec3(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]));
    }

    private TextureCoordinate parseTextureCoordinate(String line) throws ModelFormatException {
        String[] tokens = line.substring(3).trim().split(" ");
        if (tokens.length < 2) {
            throw new ModelFormatException("Invalid texture coordinate format");
        }
        return new TextureCoordinate(Float.parseFloat(tokens[0]), 1.0F - Float.parseFloat(tokens[1]));
    }

    private Face parseFace(String line) throws ModelFormatException {
        String[] tokens = line.substring(2).trim().split(" ");
        Face face = new Face();
        face.vertices = new Vec3[tokens.length];

        for (int i = 0; i < tokens.length; i++) {
            String[] indices = tokens[i].split("/");
            int vertexIndex = Integer.parseInt(indices[0]) - 1;
            face.vertices[i] = vertices.get(vertexIndex);
        }

        face.faceNormal = face.calculateFaceNormal();
        return face;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void renderAll(PoseStack matrixStack, MultiBufferSource bufferSource, ResourceLocation texture, int combinedLight, int combinedOverlay) {
        RenderType renderType = RenderType.entityCutout(texture);
        VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);

        for (GroupObject group : groupObjects) {
            group.render(matrixStack, vertexConsumer, combinedLight, combinedOverlay);
        }
    }

    @Override
    public String getType() {
        return "obj";
    }
}
