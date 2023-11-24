document.addEventListener("DOMContentLoaded",() => {
    let imgGrid = new ImageGrid({ id: "one" });
});

class ImageGrid {
    constructor(args) {
        this.container = $(`#${args.id}`);
        this.blockClass = "image-grid";
        this.cardContent = null;
        this.page = 1;
        this.imagesPerPage = 20;
        this.observer = new IntersectionObserver(
            (entries, self) => {
                entries.forEach(entry => {
                    if (entry.isIntersecting) {
                        self.unobserve(entry.target);
                        let cards = this.getCards();
                        let cardCount = cards.length;
                        let cardsAtPageStart = (this.page - 1) * this.imagesPerPage;
                        let contentIndex = cardCount - cardsAtPageStart;
                        if (cardCount < cardsAtPageStart + this.cardContent.length) {
                            this.addCard(this.cardContent[contentIndex]);
                            cards = this.getCards();
                            self.observe(cards[cardCount]);
                        } else {
                            ++this.page;
                            this.requestImages(keyword,this.imagesPerPage, this.page);
                        }
                    }
                });
            },
            {
                root: null,
                rootMargin: "0px 0px 0px 0px",
                threshold: 0.99
            }
        );
        this.createStatus();
        this.requestImages(keyword,this.imagesPerPage, this.page);
    }

    createStatus() {
        this.status = $(`<div class="${this.blockClass}__status"></div>`);
        this.container.append(this.status);
        var preloader = $(`<div class="pl pl-fade"></div>`);
        var statusMsg = $(`<p>Loading…</p>`);
        this.status.append(preloader);
        this.status.append(statusMsg);
    }

    setStatus(msg) {
        if (this.status !== null) {
            this.status.find(".pl").remove();
            this.status.find("p").html(msg);
        }
    }

    killStatus() {
        if (this.status !== null) {
            this.status.parent().children().first().remove();
            this.status = null;
        }
    }

    requestImages(keyword,perPage, page) {
        var minWidth = 270;
        var minHeight = 180;
        var url = `/posting/search?keyword=${keyword}page=${page}&size=${perPage}`;

        $.ajax({
            url: url,
            method: "POST",
            success: (data) => {
                console.log("리퀘스트 호출 성공");
                if (this.cardContent === null) {
                    this.cardContent = data;
                } else {
                    // 중복된 데이터를 제외하고 추가
                    data.forEach(newData => {
                        if (!this.cardContent.some(existingData => existingData.pageURL === newData.pageURL)) {
                            this.cardContent.push(newData);
                        }
                    });
                }

                console.log(this.cardContent + "데이터정보");
                console.log(this.cardContent.length + "카드콘텐츠길이");
                if (this.cardContent !== null && this.cardContent.length) {
                    this.killStatus();
                    this.addCard(this.cardContent[0]);
                    var firstCard = this.container.children().last();
                    this.observer.observe(firstCard[0]);
                } else {
                    this.setStatus("Nothing to show here…");
                }
            },
            error: () => {
                this.setStatus("It appears you’re offline. Check your connection and try again.");
            }
        });
    }


    addCard(content) {
        // 이미 존재하는 카드인지 확인
        if (this.container.find(`[href="${content.pageURL}"]`).length === 0) {
            // 중복되지 않는 경우에만 카드 추가
            var data = {
                title: content.tags || "untitled",
                link: content.pageURL || "#0",
                thumbnail: (content.webformatURL ? content.webformatURL.replace("640.jpg", "180.jpg") : "https://i.ibb.co/6Whjrmx/placeholder.png"),
                thumbWidth: (content.webformatWidth ? content.webformatWidth / 2 : 1),
                thumbHeight: (content.webformatHeight ? content.webformatHeight / 2 : 1)
            };
            console.log(JSON.stringify(data) + "콘텐츠를 데이터로 변환");

            var card = $(`<div class="${this.blockClass}__card"></div>`);
            this.container.append(card);
            var thumbLink = $(`<a href="${data.link}" rel="noopener noreferrer" target="_blank"></a>`);

            card.append(thumbLink);
            var thumb = $(`<img src="${data.thumbnail}" width="${data.thumbWidth}" height="${data.thumbHeight}" alt="${data.title}" class="${this.blockClass}__card-thumb"></img>`);
            if (data.thumbWidth < data.thumbHeight) {
                thumb.addClass(`${this.blockClass}__card-thumb--portrait`);
            }
            thumbLink.append(thumb);
            var cardTitle = $(`<span title="${data.title}" class="${this.blockClass}__card-title"></span>`);
            card.append(cardTitle);
            cardTitle.text(data.title);
        }
    }

    getCards() {
        return this.container.find(`.${this.blockClass}__card`);
    }

}