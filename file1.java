import java.io.*;
import java.math.BigInteger;
import java.util.*;

public class One {
    static class Point {
        BigInteger x, y;
        Point(BigInteger x, BigInteger y) {
            this.x = x; this.y = y;
        }
    }

    static BigInteger decode(String value, int base) {
        return new BigInteger(value, base);
    }

    static BigInteger lagrange(List<Point> pts, int k) {
        BigInteger res = BigInteger.ZERO;
        for (int i = 0; i < k; i++) {
            BigInteger xi = pts.get(i).x;
            BigInteger yi = pts.get(i).y;
            BigInteger term = yi;
            for (int j = 0; j < k; j++) {
                if (i == j) continue;
                BigInteger xj = pts.get(j).x;
                BigInteger num = xj.negate();
                BigInteger den = xi.subtract(xj);
                term = term.multiply(num).divide(den);
            }
            res = res.add(term);
        }
        return res;
    }

    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("input.json"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
            String json = sb.toString().replaceAll("\\s+", "");
            br.close();

            int n = Integer.parseInt(json.split("\"n\":")[1].split(",")[0]);
            int k = Integer.parseInt(json.split("\"k\":")[1].split("}")[0]);

            List<Point> pts = new ArrayList<>();
            for (int idx = 1; idx <= n; idx++) {
                if (!json.contains("\"" + idx + "\":")) continue;
                String section = json.split("\"" + idx + "\":\\{")[1].split("}")[0];
                int base = Integer.parseInt(section.split("\"base\":\"?")[1].split("\"|,")[0]);
                String val = section.split("\"value\":\"?")[1].split("\"")[0];
                pts.add(new Point(BigInteger.valueOf(idx), decode(val, base)));
            }

            pts.sort(Comparator.comparing(p -> p.x));
            List<Point> sub = pts.subList(0, k);
            BigInteger secret = lagrange(sub, k);
            System.out.println(secret);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
