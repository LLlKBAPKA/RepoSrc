package net.minecraft.world.gen.feature.structure;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPatternRegistry;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.template.ProcessorLists;

public class SnowyVillagePools {
    public static final JigsawPattern field_244129_a = JigsawPatternRegistry.func_244094_a(new JigsawPattern(new ResourceLocation("village/snowy/town_centers"), new ResourceLocation("empty"), ImmutableList.of(Pair.of(JigsawPiece.func_242849_a("village/snowy/town_centers/snowy_meeting_point_1"), 100), Pair.of(JigsawPiece.func_242849_a("village/snowy/town_centers/snowy_meeting_point_2"), 50), Pair.of(JigsawPiece.func_242849_a("village/snowy/town_centers/snowy_meeting_point_3"), 150), Pair.of(JigsawPiece.func_242849_a("village/snowy/zombie/town_centers/snowy_meeting_point_1"), 2), Pair.of(JigsawPiece.func_242849_a("village/snowy/zombie/town_centers/snowy_meeting_point_2"), 1), Pair.of(JigsawPiece.func_242849_a("village/snowy/zombie/town_centers/snowy_meeting_point_3"), 3)), JigsawPattern.PlacementBehaviour.RIGID));

    public static void init() {
    }

    static {
        JigsawPatternRegistry.func_244094_a(new JigsawPattern(new ResourceLocation("village/snowy/streets"), new ResourceLocation("village/snowy/terminators"), ImmutableList.of(Pair.of(JigsawPiece.func_242851_a("village/snowy/streets/corner_01", ProcessorLists.field_244112_l), 2), Pair.of(JigsawPiece.func_242851_a("village/snowy/streets/corner_02", ProcessorLists.field_244112_l), 2), Pair.of(JigsawPiece.func_242851_a("village/snowy/streets/corner_03", ProcessorLists.field_244112_l), 2), Pair.of(JigsawPiece.func_242851_a("village/snowy/streets/square_01", ProcessorLists.field_244112_l), 2), Pair.of(JigsawPiece.func_242851_a("village/snowy/streets/straight_01", ProcessorLists.field_244112_l), 4), Pair.of(JigsawPiece.func_242851_a("village/snowy/streets/straight_02", ProcessorLists.field_244112_l), 4), Pair.of(JigsawPiece.func_242851_a("village/snowy/streets/straight_03", ProcessorLists.field_244112_l), 4), Pair.of(JigsawPiece.func_242851_a("village/snowy/streets/straight_04", ProcessorLists.field_244112_l), 7), Pair.of(JigsawPiece.func_242851_a("village/snowy/streets/straight_06", ProcessorLists.field_244112_l), 4), Pair.of(JigsawPiece.func_242851_a("village/snowy/streets/straight_08", ProcessorLists.field_244112_l), 4), Pair.of(JigsawPiece.func_242851_a("village/snowy/streets/crossroad_02", ProcessorLists.field_244112_l), 1), Pair.of(JigsawPiece.func_242851_a("village/snowy/streets/crossroad_03", ProcessorLists.field_244112_l), 2), Pair.of(JigsawPiece.func_242851_a("village/snowy/streets/crossroad_04", ProcessorLists.field_244112_l), 2), Pair.of(JigsawPiece.func_242851_a("village/snowy/streets/crossroad_05", ProcessorLists.field_244112_l), 2), Pair.of(JigsawPiece.func_242851_a("village/snowy/streets/crossroad_06", ProcessorLists.field_244112_l), 2), Pair.of(JigsawPiece.func_242851_a("village/snowy/streets/turn_01", ProcessorLists.field_244112_l), 3)), JigsawPattern.PlacementBehaviour.TERRAIN_MATCHING));
        JigsawPatternRegistry.func_244094_a(new JigsawPattern(new ResourceLocation("village/snowy/zombie/streets"), new ResourceLocation("village/snowy/terminators"), ImmutableList.of(Pair.of(JigsawPiece.func_242851_a("village/snowy/zombie/streets/corner_01", ProcessorLists.field_244112_l), 2), Pair.of(JigsawPiece.func_242851_a("village/snowy/zombie/streets/corner_02", ProcessorLists.field_244112_l), 2), Pair.of(JigsawPiece.func_242851_a("village/snowy/zombie/streets/corner_03", ProcessorLists.field_244112_l), 2), Pair.of(JigsawPiece.func_242851_a("village/snowy/zombie/streets/square_01", ProcessorLists.field_244112_l), 2), Pair.of(JigsawPiece.func_242851_a("village/snowy/zombie/streets/straight_01", ProcessorLists.field_244112_l), 4), Pair.of(JigsawPiece.func_242851_a("village/snowy/zombie/streets/straight_02", ProcessorLists.field_244112_l), 4), Pair.of(JigsawPiece.func_242851_a("village/snowy/zombie/streets/straight_03", ProcessorLists.field_244112_l), 4), Pair.of(JigsawPiece.func_242851_a("village/snowy/zombie/streets/straight_04", ProcessorLists.field_244112_l), 7), Pair.of(JigsawPiece.func_242851_a("village/snowy/zombie/streets/straight_06", ProcessorLists.field_244112_l), 4), Pair.of(JigsawPiece.func_242851_a("village/snowy/zombie/streets/straight_08", ProcessorLists.field_244112_l), 4), Pair.of(JigsawPiece.func_242851_a("village/snowy/zombie/streets/crossroad_02", ProcessorLists.field_244112_l), 1), Pair.of(JigsawPiece.func_242851_a("village/snowy/zombie/streets/crossroad_03", ProcessorLists.field_244112_l), 2), Pair.of(JigsawPiece.func_242851_a("village/snowy/zombie/streets/crossroad_04", ProcessorLists.field_244112_l), 2), Pair.of(JigsawPiece.func_242851_a("village/snowy/zombie/streets/crossroad_05", ProcessorLists.field_244112_l), 2), Pair.of(JigsawPiece.func_242851_a("village/snowy/zombie/streets/crossroad_06", ProcessorLists.field_244112_l), 2), Pair.of(JigsawPiece.func_242851_a("village/snowy/zombie/streets/turn_01", ProcessorLists.field_244112_l), 3)), JigsawPattern.PlacementBehaviour.TERRAIN_MATCHING));
        JigsawPatternRegistry.func_244094_a(new JigsawPattern(new ResourceLocation("village/snowy/houses"), new ResourceLocation("village/snowy/terminators"), ImmutableList.of(Pair.of(JigsawPiece.func_242849_a("village/snowy/houses/snowy_small_house_1"), 2), Pair.of(JigsawPiece.func_242849_a("village/snowy/houses/snowy_small_house_2"), 2), Pair.of(JigsawPiece.func_242849_a("village/snowy/houses/snowy_small_house_3"), 2), Pair.of(JigsawPiece.func_242849_a("village/snowy/houses/snowy_small_house_4"), 3), Pair.of(JigsawPiece.func_242849_a("village/snowy/houses/snowy_small_house_5"), 2), Pair.of(JigsawPiece.func_242849_a("village/snowy/houses/snowy_small_house_6"), 2), Pair.of(JigsawPiece.func_242849_a("village/snowy/houses/snowy_small_house_7"), 2), Pair.of(JigsawPiece.func_242849_a("village/snowy/houses/snowy_small_house_8"), 2), Pair.of(JigsawPiece.func_242849_a("village/snowy/houses/snowy_medium_house_1"), 2), Pair.of(JigsawPiece.func_242849_a("village/snowy/houses/snowy_medium_house_2"), 2), Pair.of(JigsawPiece.func_242849_a("village/snowy/houses/snowy_medium_house_3"), 2), Pair.of(JigsawPiece.func_242849_a("village/snowy/houses/snowy_butchers_shop_1"), 2), Pair.of(JigsawPiece.func_242849_a("village/snowy/houses/snowy_butchers_shop_2"), 2), Pair.of(JigsawPiece.func_242849_a("village/snowy/houses/snowy_tool_smith_1"), 2), Pair.of(JigsawPiece.func_242849_a("village/snowy/houses/snowy_fletcher_house_1"), 2), Pair.of(JigsawPiece.func_242849_a("village/snowy/houses/snowy_shepherds_house_1"), 3), Pair.of(JigsawPiece.func_242849_a("village/snowy/houses/snowy_armorer_house_1"), 1), Pair.of(JigsawPiece.func_242849_a("village/snowy/houses/snowy_armorer_house_2"), 1), Pair.of(JigsawPiece.func_242849_a("village/snowy/houses/snowy_fisher_cottage"), 2), Pair.of(JigsawPiece.func_242849_a("village/snowy/houses/snowy_tannery_1"), 2), Pair.of(JigsawPiece.func_242849_a("village/snowy/houses/snowy_cartographer_house_1"), 2), Pair.of(JigsawPiece.func_242849_a("village/snowy/houses/snowy_library_1"), 2), Pair.of(JigsawPiece.func_242849_a("village/snowy/houses/snowy_masons_house_1"), 2), Pair.of(JigsawPiece.func_242849_a("village/snowy/houses/snowy_masons_house_2"), 2), Pair.of(JigsawPiece.func_242849_a("village/snowy/houses/snowy_weapon_smith_1"), 2), Pair.of(JigsawPiece.func_242849_a("village/snowy/houses/snowy_temple_1"), 2), Pair.of(JigsawPiece.func_242851_a("village/snowy/houses/snowy_farm_1", ProcessorLists.field_244115_o), 3), Pair.of(JigsawPiece.func_242851_a("village/snowy/houses/snowy_farm_2", ProcessorLists.field_244115_o), 3), Pair.of(JigsawPiece.func_242849_a("village/snowy/houses/snowy_animal_pen_1"), 2), Pair.of(JigsawPiece.func_242849_a("village/snowy/houses/snowy_animal_pen_2"), 2), Pair.of(JigsawPiece.func_242864_g(), 6)), JigsawPattern.PlacementBehaviour.RIGID));
        JigsawPatternRegistry.func_244094_a(new JigsawPattern(new ResourceLocation("village/snowy/zombie/houses"), new ResourceLocation("village/snowy/terminators"), ImmutableList.of(Pair.of(JigsawPiece.func_242851_a("village/snowy/zombie/houses/snowy_small_house_1", ProcessorLists.field_244104_d), 2), Pair.of(JigsawPiece.func_242851_a("village/snowy/zombie/houses/snowy_small_house_2", ProcessorLists.field_244104_d), 2), Pair.of(JigsawPiece.func_242851_a("village/snowy/zombie/houses/snowy_small_house_3", ProcessorLists.field_244104_d), 2), Pair.of(JigsawPiece.func_242851_a("village/snowy/zombie/houses/snowy_small_house_4", ProcessorLists.field_244104_d), 2), Pair.of(JigsawPiece.func_242851_a("village/snowy/zombie/houses/snowy_small_house_5", ProcessorLists.field_244104_d), 2), Pair.of(JigsawPiece.func_242851_a("village/snowy/zombie/houses/snowy_small_house_6", ProcessorLists.field_244104_d), 2), Pair.of(JigsawPiece.func_242851_a("village/snowy/zombie/houses/snowy_small_house_7", ProcessorLists.field_244104_d), 2), Pair.of(JigsawPiece.func_242851_a("village/snowy/zombie/houses/snowy_small_house_8", ProcessorLists.field_244104_d), 2), Pair.of(JigsawPiece.func_242851_a("village/snowy/zombie/houses/snowy_medium_house_1", ProcessorLists.field_244104_d), 2), Pair.of(JigsawPiece.func_242851_a("village/snowy/zombie/houses/snowy_medium_house_2", ProcessorLists.field_244104_d), 2), Pair.of(JigsawPiece.func_242851_a("village/snowy/zombie/houses/snowy_medium_house_3", ProcessorLists.field_244104_d), 1), Pair.of(JigsawPiece.func_242851_a("village/snowy/houses/snowy_butchers_shop_1", ProcessorLists.field_244104_d), 2), Pair.of(JigsawPiece.func_242851_a("village/snowy/houses/snowy_butchers_shop_2", ProcessorLists.field_244104_d), 2), Pair.of(JigsawPiece.func_242851_a("village/snowy/houses/snowy_tool_smith_1", ProcessorLists.field_244104_d), 2), Pair.of(JigsawPiece.func_242851_a("village/snowy/houses/snowy_fletcher_house_1", ProcessorLists.field_244104_d), 2), Pair.of(JigsawPiece.func_242851_a("village/snowy/houses/snowy_shepherds_house_1", ProcessorLists.field_244104_d), 2), Pair.of(JigsawPiece.func_242851_a("village/snowy/houses/snowy_armorer_house_1", ProcessorLists.field_244104_d), 1), Pair.of(JigsawPiece.func_242851_a("village/snowy/houses/snowy_armorer_house_2", ProcessorLists.field_244104_d), 1), Pair.of(JigsawPiece.func_242851_a("village/snowy/houses/snowy_fisher_cottage", ProcessorLists.field_244104_d), 2), Pair.of(JigsawPiece.func_242851_a("village/snowy/houses/snowy_tannery_1", ProcessorLists.field_244104_d), 2), Pair.of(JigsawPiece.func_242851_a("village/snowy/houses/snowy_cartographer_house_1", ProcessorLists.field_244104_d), 2), Pair.of(JigsawPiece.func_242851_a("village/snowy/houses/snowy_library_1", ProcessorLists.field_244104_d), 2), Pair.of(JigsawPiece.func_242851_a("village/snowy/houses/snowy_masons_house_1", ProcessorLists.field_244104_d), 2), Pair.of(JigsawPiece.func_242851_a("village/snowy/houses/snowy_masons_house_2", ProcessorLists.field_244104_d), 2), Pair.of(JigsawPiece.func_242851_a("village/snowy/houses/snowy_weapon_smith_1", ProcessorLists.field_244104_d), 2), Pair.of(JigsawPiece.func_242851_a("village/snowy/houses/snowy_temple_1", ProcessorLists.field_244104_d), 2), Pair.of(JigsawPiece.func_242851_a("village/snowy/houses/snowy_farm_1", ProcessorLists.field_244104_d), 3), Pair.of(JigsawPiece.func_242851_a("village/snowy/houses/snowy_farm_2", ProcessorLists.field_244104_d), 3), Pair.of(JigsawPiece.func_242851_a("village/snowy/houses/snowy_animal_pen_1", ProcessorLists.field_244104_d), 2), Pair.of(JigsawPiece.func_242851_a("village/snowy/houses/snowy_animal_pen_2", ProcessorLists.field_244104_d), 2), Pair.of(JigsawPiece.func_242864_g(), 6)), JigsawPattern.PlacementBehaviour.RIGID));
        JigsawPatternRegistry.func_244094_a(new JigsawPattern(new ResourceLocation("village/snowy/terminators"), new ResourceLocation("empty"), ImmutableList.of(Pair.of(JigsawPiece.func_242851_a("village/plains/terminators/terminator_01", ProcessorLists.field_244112_l), 1), Pair.of(JigsawPiece.func_242851_a("village/plains/terminators/terminator_02", ProcessorLists.field_244112_l), 1), Pair.of(JigsawPiece.func_242851_a("village/plains/terminators/terminator_03", ProcessorLists.field_244112_l), 1), Pair.of(JigsawPiece.func_242851_a("village/plains/terminators/terminator_04", ProcessorLists.field_244112_l), 1)), JigsawPattern.PlacementBehaviour.TERRAIN_MATCHING));
        JigsawPatternRegistry.func_244094_a(new JigsawPattern(new ResourceLocation("village/snowy/trees"), new ResourceLocation("empty"), ImmutableList.of(Pair.of(JigsawPiece.func_242845_a(Features.SPRUCE), 1)), JigsawPattern.PlacementBehaviour.RIGID));
        JigsawPatternRegistry.func_244094_a(new JigsawPattern(new ResourceLocation("village/snowy/decor"), new ResourceLocation("empty"), ImmutableList.of(Pair.of(JigsawPiece.func_242849_a("village/snowy/snowy_lamp_post_01"), 4), Pair.of(JigsawPiece.func_242849_a("village/snowy/snowy_lamp_post_02"), 4), Pair.of(JigsawPiece.func_242849_a("village/snowy/snowy_lamp_post_03"), 1), Pair.of(JigsawPiece.func_242845_a(Features.SPRUCE), 4), Pair.of(JigsawPiece.func_242845_a(Features.PILE_SNOW), 4), Pair.of(JigsawPiece.func_242845_a(Features.PILE_ICE), 1), Pair.of(JigsawPiece.func_242864_g(), 9)), JigsawPattern.PlacementBehaviour.RIGID));
        JigsawPatternRegistry.func_244094_a(new JigsawPattern(new ResourceLocation("village/snowy/zombie/decor"), new ResourceLocation("empty"), ImmutableList.of(Pair.of(JigsawPiece.func_242851_a("village/snowy/snowy_lamp_post_01", ProcessorLists.field_244104_d), 1), Pair.of(JigsawPiece.func_242851_a("village/snowy/snowy_lamp_post_02", ProcessorLists.field_244104_d), 1), Pair.of(JigsawPiece.func_242851_a("village/snowy/snowy_lamp_post_03", ProcessorLists.field_244104_d), 1), Pair.of(JigsawPiece.func_242845_a(Features.SPRUCE), 4), Pair.of(JigsawPiece.func_242845_a(Features.PILE_SNOW), 4), Pair.of(JigsawPiece.func_242845_a(Features.PILE_ICE), 4), Pair.of(JigsawPiece.func_242864_g(), 7)), JigsawPattern.PlacementBehaviour.RIGID));
        JigsawPatternRegistry.func_244094_a(new JigsawPattern(new ResourceLocation("village/snowy/villagers"), new ResourceLocation("empty"), ImmutableList.of(Pair.of(JigsawPiece.func_242849_a("village/snowy/villagers/nitwit"), 1), Pair.of(JigsawPiece.func_242849_a("village/snowy/villagers/baby"), 1), Pair.of(JigsawPiece.func_242849_a("village/snowy/villagers/unemployed"), 10)), JigsawPattern.PlacementBehaviour.RIGID));
        JigsawPatternRegistry.func_244094_a(new JigsawPattern(new ResourceLocation("village/snowy/zombie/villagers"), new ResourceLocation("empty"), ImmutableList.of(Pair.of(JigsawPiece.func_242849_a("village/snowy/zombie/villagers/nitwit"), 1), Pair.of(JigsawPiece.func_242849_a("village/snowy/zombie/villagers/unemployed"), 10)), JigsawPattern.PlacementBehaviour.RIGID));
    }
}
