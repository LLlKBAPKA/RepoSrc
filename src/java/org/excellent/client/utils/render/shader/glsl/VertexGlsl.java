package org.excellent.client.utils.render.shader.glsl;


import org.excellent.client.utils.render.shader.IShader;

public class VertexGlsl implements IShader {


    @Override
    public String shader() {
        return """
                #version 120
                                
                varying vec4 VertexColor;
                 
                void main() {
                    gl_TexCoord[0] = gl_MultiTexCoord0;
                    VertexColor = gl_Color;
                    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
                }""";
    }
}
