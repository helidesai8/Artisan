import { AgChartsReact } from "ag-charts-react";

function ArtistMonthlyInsight(data) {
    
        var monthlyData = {
            title: {
                text: "Monthly Sales($)",
            },
            data: data.data,
            series: [{ type: "bar", xKey: "month", yKey: "amount", xName: "month", yName: "Total Amount" }],
            background: {
                fill: 'rgb(246, 246, 246)',
            },
            overlays: {
                noData: {
                  text: "No Sales recorded Yet",
                },
              },
    
        };
    return(
        <div>
                <AgChartsReact options={monthlyData} />
        </div>
    )
};

export default ArtistMonthlyInsight;