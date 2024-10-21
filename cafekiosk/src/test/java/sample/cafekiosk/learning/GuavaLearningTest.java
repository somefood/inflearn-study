package sample.cafekiosk.learning;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

class GuavaLearningTest {

    @DisplayName("주어진 개수만큼 List를 파티셔닝한다.")
    @Test
    void partitionLearningTest1() {
        // given
        final List<Integer> integers = List.of(1, 2, 3, 4, 5, 6);

        // when
        final List<List<Integer>> partition = Lists.partition(integers, 3);

        // then
        assertThat(partition).hasSize(2)
            .isEqualTo(List.of(
                List.of(1, 2, 3),
                List.of(4, 5, 6)
            ));
    }

    @DisplayName("주어진 개수만큼 List를 파티셔닝한다.")
    @Test
    void partitionLearningTest2() {
        // given
        final List<Integer> integers = List.of(1, 2, 3, 4, 5, 6);

        // when
        final List<List<Integer>> partition = Lists.partition(integers, 4);

        // then
        assertThat(partition).hasSize(2)
            .isEqualTo(List.of(
                List.of(1, 2, 3, 4),
                List.of(5, 6)
            ));
    }

    @DisplayName("멀티맵 기능 확인")
    @Test
    void multiMapLearningTest1() {
        // given
        Multimap<String, String> multiMap = ArrayListMultimap.create();
        multiMap.put("커피", "아메리카노");
        multiMap.put("커피", "카페라떼");
        multiMap.put("커피", "카푸치노");
        multiMap.put("베이커리", "크루아상");
        multiMap.put("베이커리", "식빵");

        // when
        final Collection<String> strings = multiMap.get("커피");

        // then
        assertThat(strings).hasSize(3)
            .isEqualTo(List.of("아메리카노", "카페라떼", "카푸치노"));
    }

    @DisplayName("멀티맵 기능 확인")
    @TestFactory
    Collection<DynamicTest> multiMapLearningTest2() {
        // given
        Multimap<String, String> multiMap = ArrayListMultimap.create();
        multiMap.put("커피", "아메리카노");
        multiMap.put("커피", "카페라떼");
        multiMap.put("커피", "카푸치노");
        multiMap.put("베이커리", "크루아상");
        multiMap.put("베이커리", "식빵");

        // when
        return List.of(
            DynamicTest.dynamicTest("1개 value 삭제", () -> {
                // gwhen
                multiMap.remove("커피", "카푸치노");

                // then
                final Collection<String> results = multiMap.get("커피");
                assertThat(results).hasSize(2)
                    .isEqualTo(List.of("아메리카노", "카페라떼"));
            }),

            DynamicTest.dynamicTest("key 전부 삭제", () -> {
                // when
                multiMap.removeAll("커피");

                // then
                final Collection<String> results = multiMap.get("커피");
                assertThat(results).isEmpty();
            })
        );
    }
}
