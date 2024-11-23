package com.sw.ecogrowbackend.common;

import lombok.Getter;

@Getter
public enum WasteReductionTipMessage {

    // 쓰레기 감소 팁 메시지
    PLASTIC_REDUCTION_TIP(
        "일회용 플라스틱 사용을 줄이고, 장바구니, 텀블러, 재사용 가능한 용기를 사용해보세요. 플라스틱 소비를 줄임으로써 해양 생태계를 보호할 수 있습니다."),
    GLASS_REDUCTION_TIP(
        "유리병은 재활용이 가능하니 별도로 분리 배출해 주세요. 유리 용기를 재활용하거나 재사용하면, 새 유리를 만드는 에너지를 절약할 수 있습니다."),
    GENERAL_WASTE_REDUCTION_TIP(
        "과도한 포장재가 없는 제품을 구매하고, 재활용 가능한 소재의 제품을 선택하세요. 이를 통해 매립지 쓰레기 양을 줄이는 데 기여할 수 있습니다."),
    METAL_REDUCTION_TIP(
        "금속 캔, 알루미늄 용기는 잘 씻어서 분리 배출해 주세요. 금속 재활용을 통해 에너지 절약에 기여하고 온실가스 배출을 줄일 수 있습니다."),
    FOOD_REDUCTION_TIP(
        "음식물 쓰레기는 가능한 퇴비화하여 유용한 자원으로 활용하세요. 음식물 쓰레기를 퇴비로 전환하면, 매립지에 가는 양을 줄일 수 있습니다."),
    PAPER_REDUCTION_TIP(
        "종이 사용을 줄이기 위해, 양면 인쇄와 재활용 종이 사용을 실천해보세요. 재활용 가능한 종이를 사용하면, 나무를 보호하고 자원 낭비를 줄일 수 있습니다."),
    RECYCLABLE_PROPORTION_TIP(
        "재활용 가능한 품목을 올바르게 분류하고 배출해 주세요. 특히 플라스틱, 유리, 금속은 분리 배출해 재활용률을 높일 수 있습니다."),
    INCREASING_EMISSION_TREND_TIP(
        "최근 쓰레기 배출량이 꾸준히 증가하고 있습니다. 일상에서 재사용 가능한 제품을 선택하고 불필요한 소비를 줄여 쓰레기 배출량을 낮춰보세요."),
    MONTHLY_INCREASING_EMISSION_TREND_TIP(
        "지난 달 보다 쓰레기 배출량이 증가하고 있습니다. 월별 배출량을 줄이기 위해 재사용 제품을 선택하고 불필요한 소비를 줄여보세요.");


    private final String msg;

    WasteReductionTipMessage(String msg) {
        this.msg = msg;
    }
}