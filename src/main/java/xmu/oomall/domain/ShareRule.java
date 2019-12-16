package xmu.oomall.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class ShareRule extends ShareRulePo {
    @Getter
    @Setter
    private class Strategy{
        private Integer lowerBound;
        private Integer upperBound;
        private BigDecimal discountRate;
    }
    private List<Strategy> strategyList;
    //TODO:JSON解析
}
