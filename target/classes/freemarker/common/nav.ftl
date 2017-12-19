
<nav class="navbar navbar-default navbar-static-top">
<ul class="nav nav-tabs">
  <li role="presentation" class="active"><a href="#">Book Keeping</a></li>
  <li role="presentation"><a href="#">Home</a></li>
  <li role="presentation"><a href="#">Profile</a></li>
  <li role="presentation"><a href="#">DailySummary</a></li>
  <li role="presentation" class="dropdown">
    <a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">
      Kisaan<span class="caret"></span>
    </a>
    <ul class="dropdown-menu" aria-labelledby="dLabel">
        <li><a href="/addKisaan">Add</a></li>
        <li><a href="/removeKisaan">Remove</a></li>
        <li><a href="/listKisaan">List</a></li>
        <form action="/searchKisaan" method="POST" class="navbar-form navbar-left" role="search">
                            <div class="form-group">
                              <input type="text" class="form-control" placeholder="Search">
                            </div>
                            <button type="submit" class="btn btn-default">Submit</button>
        </form>
    </ul>
  </li>
  <li role="presentation" class="dropdown">
    <a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">
      KisaanTransaction<span class="caret"></span>
    </a>
    <ul class="dropdown-menu" aria-labelledby="dLabel">
        <li><a href="/addKisaanTransaction">Add</a></li>
        <li><a href="/removeKisaanTransaction">Remove</a></li>
        <li><a href="/listKisaanTransaction">List</a></li>
        <form action="/searchKisaanTransaction" method="POST" class="navbar-form navbar-left" role="search">
                            <div class="form-group">
                              <input type="text" class="form-control" placeholder="Search">
                            </div>
                            <button type="submit" class="btn btn-default">Submit</button>
        </form>
    </ul>
  </li>
  <li role="presentation" class="dropdown">
    <a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">
      KisaanPayment<span class="caret"></span>
    </a>
    <ul class="dropdown-menu" aria-labelledby="dLabel">
        <li><a href="/addKisaanPayment">Add</a></li>
        <li><a href="/removeKisaanPayment">Remove</a></li>
        <li><a href="/listKisaanPayment">List</a></li>
        <form action="/searchKisaanPayment" method="POST" class="navbar-form navbar-left" role="search">
                    <div class="form-group">
                      <input type="text" class="form-control" placeholder="Search">
                    </div>
                    <button type="submit" class="btn btn-default">Submit</button>
        </form>
    </ul>
  </li>
  <li role="presentation" class="dropdown">
    <a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">
      Khareeddar<span class="caret"></span>
    </a>
    <ul class="dropdown-menu" aria-labelledby="dLabel">
        <li><a href="/addKhareeddar">Add</a></li>
        <li><a href="/removeKhareeddar">Remove</a></li>
        <li><a href="/listKhareeddar">List</a></li>
        <form action="/searchKhareeddar" method="POST" class="navbar-form navbar-left" role="search">
                    <div class="form-group">
                      <input type="text" class="form-control" placeholder="Search">
                    </div>
                    <button type="submit" class="btn btn-default">Submit</button>
        </form>
    </ul>
  </li>
  <li role="presentation" class="dropdown">
    <a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">
      KhareeddarPayment<span class="caret"></span>
    </a>
    <ul class="dropdown-menu" aria-labelledby="dLabel">
        <li><a href="/addKhareeddarPayment">Add</a></li>
        <li><a href="/removeKhareeddarPayment">Remove</a></li>
        <li><a href="/listKhareeddarPayment">List</a></li>
        <form action="/searchKhareeddarPayment" method="POST" class="navbar-form navbar-left" role="search">
            <div class="form-group">
              <input type="text" class="form-control" placeholder="Search">
            </div>
            <button type="submit" class="btn btn-default">Submit</button>
        </form>
    </ul>
  </li>
  <form action="/search" method="POST" class="navbar-form navbar-left" role="search">
    <div class="form-group">
      <input type="text" class="form-control" placeholder="Search">
    </div>
    <button type="submit" class="btn btn-default">Submit</button>
  </form>
</ul>
</nav>