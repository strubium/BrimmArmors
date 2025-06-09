package concord.loader.obj;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import concord.loader.IModelCustom;
import concord.loader.ModelFormatException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL32;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WavefrontObject implements IModelCustom {
    private static final Pattern vertexPattern = Pattern.compile("(v( (\\-){0,1}\\d+(\\.\\d+)?){3,4} *\\n)|(v( (\\-){0,1}\\d+(\\.\\d+)?){3,4} *$)");
    private static final Pattern vertexNormalPattern = Pattern.compile("(vn( (\\-){0,1}\\d+(\\.\\d+)?){3,4} *\\n)|(vn( (\\-){0,1}\\d+(\\.\\d+)?){3,4} *$)");
    private static final Pattern textureCoordinatePattern = Pattern.compile("(vt( (\\-){0,1}\\d+\\.\\d+){2,3} *\\n)|(vt( (\\-){0,1}\\d+(\\.\\d+)?){2,3} *$)");
    private static final Pattern face_V_VT_VN_Pattern = Pattern.compile("(f( \\d+/\\d+/\\d+){3,4} *\\n)|(f( \\d+/\\d+/\\d+){3,4} *$)");
    private static final Pattern face_V_VT_Pattern = Pattern.compile("(f( \\d+/\\d+){3,4} *\\n)|(f( \\d+/\\d+){3,4} *$)");
    private static final Pattern face_V_VN_Pattern = Pattern.compile("(f( \\d+//\\d+){3,4} *\\n)|(f( \\d+//\\d+){3,4} *$)");
    private static final Pattern face_V_Pattern = Pattern.compile("(f( \\d+){3,4} *\\n)|(f( \\d+){3,4} *$)");
    private static final Pattern groupObjectPattern = Pattern.compile("([go]( [\\w\\d\\.]+) *\\n)|([go]( [\\w\\d\\.]+) *$)");
    private static Matcher vertexMatcher;
    private static Matcher vertexNormalMatcher;
    private static Matcher textureCoordinateMatcher;
    private static Matcher face_V_VT_VN_Matcher;
    private static Matcher face_V_VT_Matcher;
    private static Matcher face_V_VN_Matcher;
    private static Matcher face_V_Matcher;
    private static Matcher groupObjectMatcher;
    private final String fileName;
    public ArrayList<Vertex> vertices = new ArrayList<>();
    public ArrayList<Vertex> vertexNormals = new ArrayList<>();
    public ArrayList<TextureCoordinate> textureCoordinates = new ArrayList<>();
    public ArrayList<GroupObject> groupObjects = new ArrayList<>();
    private GroupObject currentGroupObject;
    private int id = 0;
    private boolean init = false;

    public WavefrontObject(ResourceLocation resource) throws ModelFormatException {
        this.fileName = resource.toString();

        try {
            IResource res = Minecraft.getInstance().getResourceManager().getResource(resource);
            this.loadObjModel(res.getInputStream());
        } catch (IOException var3) {
            throw new ModelFormatException("IO Exception reading model format", var3);
        }
    }

    private static boolean isValidVertexLine(String line) {
        if (vertexMatcher != null) {
            vertexMatcher.reset();
        }

        vertexMatcher = vertexPattern.matcher(line);
        return vertexMatcher.matches();
    }

    private static boolean isValidVertexNormalLine(String line) {
        if (vertexNormalMatcher != null) {
            vertexNormalMatcher.reset();
        }

        vertexNormalMatcher = vertexNormalPattern.matcher(line);
        return vertexNormalMatcher.matches();
    }

    private static boolean isValidTextureCoordinateLine(String line) {
        if (textureCoordinateMatcher != null) {
            textureCoordinateMatcher.reset();
        }

        textureCoordinateMatcher = textureCoordinatePattern.matcher(line);
        return textureCoordinateMatcher.matches();
    }

    private static boolean isValidFace_V_VT_VN_Line(String line) {
        if (face_V_VT_VN_Matcher != null) {
            face_V_VT_VN_Matcher.reset();
        }

        face_V_VT_VN_Matcher = face_V_VT_VN_Pattern.matcher(line);
        return face_V_VT_VN_Matcher.matches();
    }

    private static boolean isValidFace_V_VT_Line(String line) {
        if (face_V_VT_Matcher != null) {
            face_V_VT_Matcher.reset();
        }

        face_V_VT_Matcher = face_V_VT_Pattern.matcher(line);
        return face_V_VT_Matcher.matches();
    }

    private static boolean isValidFace_V_VN_Line(String line) {
        if (face_V_VN_Matcher != null) {
            face_V_VN_Matcher.reset();
        }

        face_V_VN_Matcher = face_V_VN_Pattern.matcher(line);
        return face_V_VN_Matcher.matches();
    }

    private static boolean isValidFace_V_Line(String line) {
        if (face_V_Matcher != null) {
            face_V_Matcher.reset();
        }

        face_V_Matcher = face_V_Pattern.matcher(line);
        return face_V_Matcher.matches();
    }

    private static boolean isValidFaceLine(String line) {
        return isValidFace_V_VT_VN_Line(line) || isValidFace_V_VT_Line(line) || isValidFace_V_VN_Line(line) || isValidFace_V_Line(line);
    }

    private static boolean isValidGroupObjectLine(String line) {
        if (groupObjectMatcher != null) {
            groupObjectMatcher.reset();
        }

        groupObjectMatcher = groupObjectPattern.matcher(line);
        return groupObjectMatcher.matches();
    }

    private void loadObjModel(InputStream inputStream) throws ModelFormatException {
        BufferedReader reader = null;
        int lineCount = 0;

        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                ++lineCount;
                currentLine = currentLine.replaceAll("\\s+", " ").trim();
                if (!currentLine.startsWith("#") && currentLine.length() != 0) {
                    Vertex vertex;
                    if (currentLine.startsWith("v ")) {
                        vertex = this.parseVertex(currentLine, lineCount);
                        if (vertex != null) {
                            this.vertices.add(vertex);
                        }
                    } else if (currentLine.startsWith("vn ")) {
                        vertex = this.parseVertexNormal(currentLine, lineCount);
                        if (vertex != null) {
                            this.vertexNormals.add(vertex);
                        }
                    } else if (currentLine.startsWith("vt ")) {
                        TextureCoordinate textureCoordinate = this.parseTextureCoordinate(currentLine, lineCount);
                        if (textureCoordinate != null) {
                            this.textureCoordinates.add(textureCoordinate);
                        }
                    } else if (currentLine.startsWith("f ")) {
                        if (this.currentGroupObject == null) {
                            this.currentGroupObject = new GroupObject("Default");
                        }

                        Face face = this.parseFace(currentLine, lineCount);
                        if (face != null) {
                            this.currentGroupObject.faces.add(face);
                        }
                    } else if (currentLine.startsWith("g ") | currentLine.startsWith("o ")) {
                        GroupObject group = this.parseGroupObject(currentLine, lineCount);
                        if (group != null && this.currentGroupObject != null) {
                            this.groupObjects.add(this.currentGroupObject);
                        }

                        this.currentGroupObject = group;
                    }
                }
            }

            this.groupObjects.add(this.currentGroupObject);
        } catch (IOException var16) {
            throw new ModelFormatException("IO Exception reading model format", var16);
        } finally {
            try {
                reader.close();
            } catch (IOException var15) {
            }

            try {
                inputStream.close();
            } catch (IOException var14) {
            }

        }
        Collections.sort(groupObjects);
    }

    @OnlyIn(Dist.CLIENT)
    public void renderAll() {
        if (!this.init) {
            this.id = GL32.glGenLists(1);
            GL32.glNewList(this.id, 4864);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder builder = tessellator.getBuilder();
            if (this.currentGroupObject != null) {
                builder.begin(this.currentGroupObject.glDrawingMode, DefaultVertexFormats.POSITION_TEX);
            } else {
                builder.begin(4, DefaultVertexFormats.POSITION_TEX);
            }

            this.tessellateAll(tessellator);
            tessellator.end();
            GL32.glEndList();
            this.init = true;
        } else {
            GL32.glCallList(this.id);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void renderAll(MatrixStack matrix) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuilder();
        builder.begin(GL32.GL_TRIANGLES, DefaultVertexFormats.POSITION_TEX);
        for (GroupObject groupObject : this.groupObjects) {
            groupObject.render(builder, matrix);
        }
        tessellator.end();
    }

    @Override
    public void renderAll(ResourceLocation texture, MatrixStack matrix, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        RenderType custom = RenderType.create("obj",
                DefaultVertexFormats.POSITION_COLOR_TEX_LIGHTMAP, GL32.GL_TRIANGLES, 256,
                RenderType.State.builder()
                        .setAlphaState(new RenderState.AlphaState(0.1f))
                        .setTransparencyState(new RenderState.TransparencyState("blend", () -> {
                            GlStateManager._enableBlend();
                            GlStateManager._blendFunc(GL32.GL_SRC_ALPHA, GL32.GL_ONE_MINUS_SRC_ALPHA);
                        }, GlStateManager::_disableBlend))
                        .setTextureState(new RenderState.TextureState(texture, false, false))
                        .setLightmapState(new RenderState.LightmapState(true))
                        .setWriteMaskState(new RenderState.WriteMaskState(true, true))
                        .setCullState(new RenderState.CullState(true))
                        .createCompositeState(false));
        IVertexBuilder builder = buffer.getBuffer(custom);
        matrix.pushPose();

        for (GroupObject groupObject : this.groupObjects) {
            groupObject.render(matrix, builder, combinedLight, combinedOverlay);
        }
        matrix.popPose();
    }

    @OnlyIn(Dist.CLIENT)
    public void tessellateAll(Tessellator tessellator) {
        for (GroupObject groupObject : this.groupObjects) {
            groupObject.render(tessellator);
        }
    }

    private Vertex parseVertex(String line, int lineCount) throws ModelFormatException {
        if (isValidVertexLine(line)) {
            line = line.substring(line.indexOf(" ") + 1);
            String[] tokens = line.split(" ");

            try {
                if (tokens.length == 2) {
                    return new Vertex(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]));
                } else {
                    return tokens.length == 3 ? new Vertex(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]), Float
                            .parseFloat(tokens[2])) : null;
                }
            } catch (NumberFormatException var5) {
                throw new ModelFormatException(String.format("Number formatting error at line %d", lineCount), var5);
            }
        } else {
            throw new ModelFormatException("Error parsing entry ('" + line + "', line " + lineCount + ") in file '" + this.fileName + "' - Incorrect format");
        }
    }

    private Vertex parseVertexNormal(String line, int lineCount) throws ModelFormatException {
        if (isValidVertexNormalLine(line)) {
            line = line.substring(line.indexOf(" ") + 1);
            String[] tokens = line.split(" ");

            try {
                return tokens.length == 3 ? new Vertex(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2])) : null;
            } catch (NumberFormatException var5) {
                throw new ModelFormatException(String.format("Number formatting error at line %d", lineCount), var5);
            }
        } else {
            throw new ModelFormatException("Error parsing entry ('" + line + "', line " + lineCount + ") in file '" + this.fileName + "' - Incorrect format");
        }
    }

    private TextureCoordinate parseTextureCoordinate(String line, int lineCount) throws ModelFormatException {
        if (isValidTextureCoordinateLine(line)) {
            line = line.substring(line.indexOf(" ") + 1);
            String[] tokens = line.split(" ");

            try {
                if (tokens.length == 2) {
                    return new TextureCoordinate(Float.parseFloat(tokens[0]), 1.0F - Float.parseFloat(tokens[1]));
                } else {
                    return tokens.length == 3 ? new TextureCoordinate(Float.parseFloat(tokens[0]), 1.0F - Float.parseFloat(tokens[1]), Float
                            .parseFloat(tokens[2])) : null;
                }
            } catch (NumberFormatException var5) {
                throw new ModelFormatException(String.format("Number formatting error at line %d", lineCount), var5);
            }
        } else {
            throw new ModelFormatException("Error parsing entry ('" + line + "', line " + lineCount + ") in file '" + this.fileName + "' - Incorrect format");
        }
    }

    private Face parseFace(String line, int lineCount) throws ModelFormatException {
        if (isValidFaceLine(line)) {
            Face face = new Face();
            String trimmedLine = line.substring(line.indexOf(" ") + 1);
            String[] tokens = trimmedLine.split(" ");
            if (tokens.length == 3) {
                if (this.currentGroupObject.glDrawingMode == -1) {
                    this.currentGroupObject.glDrawingMode = 4;
                } else if (this.currentGroupObject.glDrawingMode != 4) {
                    System.err.println("Error parsing entry ('" + line + "', line " + lineCount + ") in file '" + this.fileName + "' - Invalid number of points for face (expected 4, found " + tokens.length + ")");
                }
            } else if (tokens.length == 4) {
                if (this.currentGroupObject.glDrawingMode == -1) {
                    this.currentGroupObject.glDrawingMode = 7;
                } else if (this.currentGroupObject.glDrawingMode != 7) {
                    System.err.println("Error parsing entry ('" + line + "', line " + lineCount + ") in file '" + this.fileName + "' - Invalid number of points for face (expected 3, found " + tokens.length + ")");
                }
            }

            String[] subTokens;
            int i;
            if (isValidFace_V_VT_VN_Line(line)) {
                face.vertices = new Vertex[tokens.length];
                face.textureCoordinates = new TextureCoordinate[tokens.length];
                face.vertexNormals = new Vertex[tokens.length];

                for (i = 0; i < tokens.length; ++i) {
                    subTokens = tokens[i].split("/");
                    face.vertices[i] = this.vertices.get(Integer.parseInt(subTokens[0]) - 1);
                    face.textureCoordinates[i] = this.textureCoordinates.get(Integer.parseInt(subTokens[1]) - 1);
                    face.vertexNormals[i] = this.vertexNormals.get(Integer.parseInt(subTokens[2]) - 1);
                }

                face.faceNormal = face.calculateFaceNormal();
            } else if (isValidFace_V_VT_Line(line)) {
                face.vertices = new Vertex[tokens.length];
                face.textureCoordinates = new TextureCoordinate[tokens.length];

                for (i = 0; i < tokens.length; ++i) {
                    subTokens = tokens[i].split("/");
                    face.vertices[i] = this.vertices.get(Integer.parseInt(subTokens[0]) - 1);
                    face.textureCoordinates[i] = this.textureCoordinates.get(Integer.parseInt(subTokens[1]) - 1);
                }

                face.faceNormal = face.calculateFaceNormal();
            } else if (isValidFace_V_VN_Line(line)) {
                face.vertices = new Vertex[tokens.length];
                face.vertexNormals = new Vertex[tokens.length];

                for (i = 0; i < tokens.length; ++i) {
                    subTokens = tokens[i].split("//");
                    face.vertices[i] = this.vertices.get(Integer.parseInt(subTokens[0]) - 1);
                    face.vertexNormals[i] = this.vertexNormals.get(Integer.parseInt(subTokens[1]) - 1);
                }

                face.faceNormal = face.calculateFaceNormal();
            } else {
                if (!isValidFace_V_Line(line)) {
                    throw new ModelFormatException("Error parsing entry ('" + line + "', line " + lineCount + ") in file '" + this.fileName + "' - Incorrect format");
                }

                face.vertices = new Vertex[tokens.length];

                for (i = 0; i < tokens.length; ++i) {
                    face.vertices[i] = this.vertices.get(Integer.parseInt(tokens[i]) - 1);
                }

                face.faceNormal = face.calculateFaceNormal();
            }

            return face;
        } else {
            throw new ModelFormatException("Error parsing entry ('" + line + "', line " + lineCount + ") in file '" + this.fileName + "' - Incorrect format");
        }
    }

    private GroupObject parseGroupObject(String line, int lineCount) throws ModelFormatException {
        GroupObject group = null;
        if (isValidGroupObjectLine(line)) {
            String trimmedLine = line.substring(line.indexOf(" ") + 1);
            if (trimmedLine.length() > 0) {
                group = new GroupObject(trimmedLine);
            }

            return group;
        } else {
            throw new ModelFormatException("Error parsing entry ('" + line + "', line " + lineCount + ") in file '" + this.fileName + "' - Incorrect format");
        }
    }

    public String getType() {
        return "obj";
    }

}
