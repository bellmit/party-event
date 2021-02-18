<div class="table_nav">
    <ul class="pagebottonlist">
        <li>共有{{opts.totalNum}}条信息 | 每页显示{{if opts.extra.linesPerPageEditable!=false}}<input type="text" value="{{opts.linesPerPage}}" class="setLinesPerPage showPerpage"/>{{else}}{{opts.linesPerPage}}{{/if}}条</li>
        <li class="first"><a href="javascript:;" page="1"></a></li>
        <li class="active pre_page"><a href="javascript:;" page="{{opts.currentPage-1}}"></a></li>
        <li><input type="text" value="" class="goTopage_01 setGoToPage"/>页 </li>
        <li class="next_page"><a href="javascript:;" page="{{opts.currentPage+1}}"></a></li>
        <li class="last_page"><a href="javascript:;" page="{{opts.totalPage}}"></a></li>
    </ul>
</div>