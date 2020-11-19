package server.gachapon;

/**
 *
 * @author Alan (SharpAceX) - gachapon source classes stub & pirate items
 * @author Ronan - parsed MapleSEA loots
 * 
* MapleSEA-like loots thanks to AyumiLove, src:
 * https://ayumilovemaple.wordpress.com/maplestory-gachapon-guide/
 */
public class Custom extends GachaponItems {

    @Override
    public int[] getCommonItems() {
        return new int[]{
            /* Scroll */
            1302020, 1382009, 1492020, 1482020, 1452016, 1472030, 1462014, 1092030, 1002509, 1102166,
            1382012, 1302030, 1322054, 1332025, 1412011, 1432012, 1482021, 1492021, 1452022, 1472032, 1462019, 1442024, 1092030, 1382041, 1002510, 1102167,
            1382039, 1372034, 1302064, 1332055, 1332056, 1402039, 1412027, 1422014, 1422029, 1492022, 1312032, 1482022, 1452045, 1472055, 1462040, 1442051, 1432040, 1092045, 1092046, 1092047, 1382041, 1002511, 1102168,
            1122014, 1382037, 1382041, 1382060, 1402037, 1412032, 1442057, 1442068, 1452060, 1472054, 1472062, 1003027, 1102041, 1102042, 1072344, 1092049, 1382045, 1382046, 1382047, 1382048, 1382049, 1382050, 1382051, 1382052, 1372035, 1372036, 1372037, 1372038, 1372039, 1372040, 1372041, 1372042, 1462046, 1382068, 1402062, 1442078, 1452071, 1472086, 1492037, 1332053, 1372049, 1082223, 1082246, 1022082, 1092022

        };
    }

    @Override
    public int[] getUncommonItems() {
        return new int[]{2040811, 2040815, 2101001};
    }

    @Override
    public int[] getRareItems() {
        return new int[]{};
    }

}
