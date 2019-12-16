package xmu.oomall.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class DefaultFreight extends DefaultFreightPo {

    private List<Integer> regionIds;

    //TODO: 加解析
}
