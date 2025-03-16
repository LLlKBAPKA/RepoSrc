package org.excellent.client.utils.render.shader.glsl;

import org.excellent.client.utils.render.shader.IShader;

public class KawaseDownBlurGlsl implements IShader {
    @Override
    public String shader() {
        return """
                #version 120

                uniform sampler2D inTexture;
                uniform vec2 offset, halfpixel, resolution;

                void main() {
                    vec2 uv = gl_FragCoord.xy / resolution;
                    vec4 sum = texture2D(inTexture, gl_TexCoord[0].st) * 4.0;

                    vec2 offsets[4] = vec2[](
                        vec2(-1.0, 1.0),
                        vec2(1.0, 1.0),
                        vec2(1.0, -1.0),
                        vec2(-1.0, -1.0)
                    );

                    for (int i = 0; i < 4; ++i) {
                        vec2 sampleUV = uv + offsets[i] * halfpixel * offset;
                        sum += texture2D(inTexture, sampleUV);
                    }

                    gl_FragColor = vec4(sum.rgb * 0.125, 1.0);
                }""";
    }
}
