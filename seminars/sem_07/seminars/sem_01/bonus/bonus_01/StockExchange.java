import java.util.Arrays;

public class StockExchange {
  
    public static int maxProfit(int[] prices) {
        if (prices == null || prices.length == 0) {
            return 0;
        }

        int size = prices.length;

        // maxprofit from first and second transaction:
        int[] profitLeft = new int[size];
        int[] profitRight = new int[size];

        // left profit
        int minPrice = prices[0];
        for (int i = 1; i < size; i++) {
            minPrice = Math.min(minPrice, prices[i]);
            profitLeft[i] = Math.max(profitLeft[i - 1], prices[i] - minPrice);
        }

        // right profit
        int maxPrice = prices[size - 1];
        for (int i = size - 2; i >= 0; i--) {
            maxPrice = Math.max(maxPrice, prices[i]);
            profitRight[i] = Math.max(profitRight[i + 1], maxPrice - prices[i]);
        }

        int maxProfit = 0;
        // max profit from both transactions
        for (int i = 0; i < size; i++) {
            int currProfit = profitRight[i] + profitLeft[i];
            maxProfit = Math.max(maxProfit, currProfit);
        }

        return maxProfit;
    }

    public static void main(String[] args) {
        System.out.println(maxProfit(new int[] {7, 1, 5, 3, 6, 4})); // 7
        System.out.println(maxProfit(new int[] {1, 2, 3, 4, 5})); // 4
        System.out.println(maxProfit(new int[] {7, 6, 4, 3, 1})); // 0
    }
}
