package lab.oxgame.engine;

import lab.oxgame.model.OXEnum;

public class OXGameImpl implements OXGame {

    private OXEnum kolejnosc;
    private OXEnum zwyciezca;
    private OXEnum[] stan;
    private int krok;

    public OXGameImpl() {
        inicjalizuj();
    }

    @Override
    public void inicjalizuj() {
        krok = 0;
        zwyciezca = OXEnum.BRAK;
        kolejnosc = Math.random() < 0.5 ? OXEnum.X : OXEnum.O;
        stan = new OXEnum[]{
                OXEnum.BRAK, OXEnum.BRAK, OXEnum.BRAK,
                OXEnum.BRAK, OXEnum.BRAK, OXEnum.BRAK,
                OXEnum.BRAK, OXEnum.BRAK, OXEnum.BRAK
        };
    }

    public boolean wykonajRuch(int pozycja) {
        if (pozycja < 0 || pozycja >= 9 || stan[pozycja] != OXEnum.BRAK || zwyciezca != OXEnum.BRAK) {
            return false;
        }
        stan[pozycja] = kolejnosc;
        krok++;
        sprawdzZwyciezce();
        kolejnosc = (kolejnosc == OXEnum.X) ? OXEnum.O : OXEnum.X;
        return true;
    }

    private void sprawdzZwyciezce() {
        int[][] zwycieskieKombinacje = {
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // Wiersze
                {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // Kolumny
                {0, 4, 8}, {2, 4, 6}             // Przekątne
        };

        for (int[] kombinacja : zwycieskieKombinacje) {
            if (stan[kombinacja[0]] != OXEnum.BRAK &&
                    stan[kombinacja[0]] == stan[kombinacja[1]] &&
                    stan[kombinacja[1]] == stan[kombinacja[2]]) {
                zwyciezca = stan[kombinacja[0]];
                return;
            }
        }
        if (krok == 9) {
            zwyciezca = OXEnum.BRAK; // Remis
        }
    }

    public OXEnum getZwyciezca() {
        return zwyciezca;
    }

    public OXEnum[] getStan() {
        return stan;
    }

    public OXEnum getKolejnosc() {
        return kolejnosc;
    }
}
