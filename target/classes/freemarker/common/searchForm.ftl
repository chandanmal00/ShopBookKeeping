<div class="search_form" >
    <form action="/search" method="POST">
        <label for="SearchHere">
            <span id="image" style="vertical-align: middle;">
             Search:
            </span>
        </label>
        <input type="text" name="search" id="search" size="150" value="${search!""}" placeholder="Search for your friends recommendations here...">
        <input type="submit" class="search" value="GO">
    </form>
</div>