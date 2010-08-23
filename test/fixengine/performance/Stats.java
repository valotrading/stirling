/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fixengine.performance;

public class Stats {
    public static long min(long[] numbers) {
        long result = Long.MAX_VALUE;
        for (int i = 0; i < numbers.length; i++)
            result = Math.min(result, numbers[i]);
        return result;
    }

    public static long max(long[] numbers) {
        long result = Long.MIN_VALUE;
        for (int i = 0; i < numbers.length; i++)
            result = Math.max(result, numbers[i]);
        return result;
    }

    public static double mean(long[] numbers) {
        long result = 0;
        for (int i = 0; i < numbers.length; i++)
            result += numbers[i];
        return (double) result / numbers.length;
    }

    public static double stddev(long[] a) {
        return Math.sqrt(var(a));
    }

    public static double var(long[] a) {
        if (a.length == 0) throw new RuntimeException("Array size is 0.");
        double avg = mean(a);
        double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            sum += (a[i] - avg) * (a[i] - avg);
        }
        return sum / (a.length - 1);
    }
}
